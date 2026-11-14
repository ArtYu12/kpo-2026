package pos.infrastructure.console;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import pos.application.GradingFacade;
import pos.domain.exception.DomainValidationException;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.Student;

// End to end demo scenario
final class DemoScenario {

    private static final String[][] STUDENTS = {
        {"Ivan Petrov", "BSE245"},
        {"Anna Smirnova", "BSE245"},
        {"Oleg Kuznetsov", "BSE246"},
    };
    private static final String[] CRITERIA = {
        "Attended lectures",
        "Kept deadlines",
        "Went to seminars",
        "Did not complain",
        "Used AI but understood the code",
    };
    private static final double[] MAX_SCORES = {10, 5, 10, 10, 10};
    private static final double[] WEIGHTS = {0.2, 0.2, 0.2, 0.1, 0.3};
    // Oleg has only two scores, so the rules give different grades
    private static final double[][] SCORES = {
        {9, 5, 8, 10, 9},
        {10, 4, 10, 6, 7},
        {6, 3},
    };
    private static final double TOO_BIG_SCORE = 12;
    private static final int UNKNOWN_CRITERION = 99;
    private static final double CORRECTED_SCORE = 4;
    private static final String DEMO_FILE =
            ConsoleRunner.RESULTS_FOLDER + "/demo-grades.csv";

    private final GradingFacade facade;
    private final ConsoleView view;
    private final PrintStream output;

    // Creates the scenario
    DemoScenario(
            final GradingFacade facade,
            final ConsoleView view,
            final PrintStream output
    ) {
        this.facade = facade;
        this.view = view;
        this.output = output;
    }

    void run() {
        if (!facade.students().isEmpty() || !facade.criteria().isEmpty()) {
            output.println("Demo needs empty data, restart the application");
            return;
        }

        output.println("=== 1. Students and rubric");
        List<Student> students = addStudents();
        List<Criterion> criteria = addCriteria();
        view.printStudents();
        view.printRubric();

        output.println("=== 2. Evaluations");
        List<Evaluation> evaluations = evaluate(students, criteria);
        view.printEvaluations();

        output.println("=== 3. Rules of the domain");
        showRules(students, criteria);

        output.println("=== 4. Correction of an evaluation");
        Evaluation last = evaluations.get(evaluations.size() - 1);
        facade.correctEvaluation(
                last.id(),
                CORRECTED_SCORE,
                "corrected after appeal"
        );
        output.println("  evaluation #" + last.id() + " corrected");

        output.println("=== 5. Final grades");
        view.printResults();
        facade.selectStrategy(2);
        view.printResults();
        facade.selectStrategy(1);

        output.println("=== 6. Export");
        Path file = facade.exportResults("csv", DEMO_FILE);
        output.println("  results exported to " + file);
    }

    private List<Student> addStudents() {
        List<Student> students = new ArrayList<>();
        for (String[] data : STUDENTS) {
            students.add(facade.addStudent(data[0], data[1]));
        }
        return students;
    }

    private List<Criterion> addCriteria() {
        List<Criterion> criteria = new ArrayList<>();
        for (int i = 0; i < CRITERIA.length; i++) {
            criteria.add(facade.addCriterion(
                    CRITERIA[i],
                    MAX_SCORES[i],
                    WEIGHTS[i]
            ));
        }
        return criteria;
    }

    private List<Evaluation> evaluate(
            final List<Student> students,
            final List<Criterion> criteria
    ) {
        List<Evaluation> evaluations = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < SCORES[i].length; j++) {
                evaluations.add(facade.evaluate(
                        students.get(i).id(),
                        criteria.get(j).id(),
                        SCORES[i][j],
                        ""
                ));
            }
        }
        return evaluations;
    }

    private void showRules(
            final List<Student> students,
            final List<Criterion> criteria
    ) {
        int lastStudent = students.get(students.size() - 1).id();
        Criterion lastCriterion = criteria.get(criteria.size() - 1);
        int firstStudent = students.get(0).id();
        int firstCriterion = criteria.get(0).id();

        tryAction(
                "score 12 of 10",
                () -> facade.evaluate(
                        lastStudent,
                        lastCriterion.id(),
                        TOO_BIG_SCORE,
                        ""
                )
        );
        tryAction(
                "criterion that is not in the rubric",
                () -> facade.evaluate(
                        lastStudent,
                        UNKNOWN_CRITERION,
                        1,
                        ""
                )
        );
        tryAction(
                "second evaluation for the same criterion",
                () -> facade.evaluate(firstStudent, firstCriterion, 1, "")
        );
        tryAction(
                "total weight greater than 1",
                () -> facade.addCriterion("Extra criterion", 1, 1)
        );
    }

    private void tryAction(final String description, final Runnable action) {
        try {
            action.run();
            output.println("  " + description + ": accepted");
        } catch (DomainValidationException exception) {
            output.println(
                    "  " + description + ": rejected - "
                            + exception.getMessage()
            );
        }
    }
}

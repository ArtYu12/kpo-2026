package pos.infrastructure.console;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import pos.application.GradingFacade;
import pos.application.StudentResult;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.Student;

// Prints lists and results of the module to the console
final class ConsoleView {

    private final GradingFacade facade;
    private final PrintStream output;

    // Creates the view
    ConsoleView(final GradingFacade facade, final PrintStream output) {
        this.facade = facade;
        this.output = output;
    }

    void printStudents() {
        List<Student> students = facade.students();
        output.println("Students");
        if (students.isEmpty()) {
            output.println("  no students yet");
        }
        for (Student student : students) {
            output.printf(
                    "  #%s %s, group %s%n",
                    student.id(),
                    student.fullName(),
                    student.group()
            );
        }
    }

    void printRubric() {
        List<Criterion> criteria = facade.criteria();
        output.println("Rubric");
        if (criteria.isEmpty()) {
            output.println("  no criteria yet");
        }
        for (Criterion criterion : criteria) {
            output.printf(
                    Locale.ROOT,
                    "  #%s %s, max score %.1f, weight %.2f%n",
                    criterion.id(),
                    criterion.name(),
                    criterion.maxScore(),
                    criterion.weight().value()
            );
        }
        output.printf(
                Locale.ROOT,
                "  total weight: %.2f%n",
                facade.totalWeight()
        );
    }

    void printEvaluations() {
        List<Evaluation> evaluations = facade.evaluations();
        output.println("Evaluations");
        if (evaluations.isEmpty()) {
            output.println("  no evaluations yet");
        }
        for (Evaluation evaluation : evaluations) {
            Student student = facade.student(
                    evaluation.studentId()
            );
            Criterion criterion = facade.criterion(
                    evaluation.criterionId()
            );
            output.printf(
                    Locale.ROOT,
                    "  #%s %s | %s: %.1f / %.1f | %s | %s%n",
                    evaluation.id(),
                    student.fullName(),
                    criterion.name(),
                    evaluation.score().value(),
                    evaluation.score().maxScore(),
                    evaluation.date(),
                    commentOf(evaluation)
            );
        }
    }

    void printResults() {
        List<StudentResult> results = facade.results();
        output.println("Results (" + facade.currentStrategyName() + ")");
        if (results.isEmpty()) {
            output.println("  no students yet");
        }
        for (StudentResult result : results) {
            output.printf(
                    Locale.ROOT,
                    "  #%d %s, group %s: %.2f -> %d%n",
                    result.studentId(),
                    result.fullName(),
                    result.group(),
                    result.grade(),
                    result.roundedGrade()
            );
        }
    }

    private static String commentOf(final Evaluation evaluation) {
        if (evaluation.comment().isEmpty()) {
            return "no comment";
        }
        return evaluation.comment();
    }
}

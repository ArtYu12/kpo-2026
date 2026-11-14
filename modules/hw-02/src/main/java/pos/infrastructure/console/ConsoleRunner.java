package pos.infrastructure.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pos.application.ExportException;
import pos.application.GradingFacade;
import pos.domain.exception.DomainValidationException;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.Student;

// Runs the command line menu of the module
@Component
@Profile("!test")
public final class ConsoleRunner implements CommandLineRunner {

    // Folder for exported files
    static final String RESULTS_FOLDER = "results";

    private static final String DEMO_ARGUMENT = "--demo";
    private static final String DEFAULT_FORMAT = "csv";
    private static final String DEFAULT_FILE = "grades";

    private final GradingFacade facade;
    private final Scanner scanner;
    private final PrintStream output;
    private final ConsoleView view;
    private final DemoScenario demoScenario;

    // Creates the menu for the system console
    @Autowired
    public ConsoleRunner(final GradingFacade facade) {
        this(facade, System.in, System.out);
    }

    ConsoleRunner(
            final GradingFacade facade,
            final InputStream input,
            final PrintStream printStream
    ) {
        this.facade = facade;
        this.scanner = new Scanner(input);
        this.output = printStream;
        this.view = new ConsoleView(facade, printStream);
        this.demoScenario = new DemoScenario(facade, view, printStream);
    }

    @Override
    public void run(final String... args) {
        if (List.of(args).contains(DEMO_ARGUMENT)) {
            demoScenario.run();
            return;
        }

        output.println("POS - teacher evaluation of students");
        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                return;
            }

            String command = scanner.nextLine().trim();
            try {
                if (execute(command)) {
                    return;
                }
            } catch (DomainValidationException
                     | ExportException exception) {
                output.println("Error: " + exception.getMessage());
            } catch (NumberFormatException exception) {
                output.println("Error: a number was expected");
            }
        }
    }

    private boolean execute(final String command) {
        MenuAction action = MenuAction.fromCode(command);
        return switch (action) {
            case ADD_STUDENT -> {
                addStudent();
                yield false;
            }
            case SHOW_STUDENTS -> {
                view.printStudents();
                yield false;
            }
            case REMOVE_STUDENT -> {
                removeStudent();
                yield false;
            }
            case ADD_CRITERION -> {
                addCriterion();
                yield false;
            }
            case SHOW_RUBRIC -> {
                view.printRubric();
                yield false;
            }
            case REMOVE_CRITERION -> {
                removeCriterion();
                yield false;
            }
            case EVALUATE -> {
                evaluate();
                yield false;
            }
            case SHOW_EVALUATIONS -> {
                view.printEvaluations();
                yield false;
            }
            case CORRECT_EVALUATION -> {
                correctEvaluation();
                yield false;
            }
            case CANCEL_EVALUATION -> {
                cancelEvaluation();
                yield false;
            }
            case SHOW_RESULTS -> {
                view.printResults();
                yield false;
            }
            case CHANGE_RULE -> {
                changeRule();
                yield false;
            }
            case EXPORT_RESULTS -> {
                exportResults();
                yield false;
            }
            case RUN_DEMO -> {
                demoScenario.run();
                yield false;
            }
            case EXIT -> {
                output.println("Goodbye");
                yield true;
            }
            case UNKNOWN -> {
                output.println("Unknown command");
                yield false;
            }
        };
    }

    private void printMenu() {
        output.println();
        output.println("1 - Add student");
        output.println("2 - Show students");
        output.println("3 - Remove student");
        output.println("4 - Add criterion");
        output.println("5 - Show rubric");
        output.println("6 - Remove criterion");
        output.println("7 - Evaluate student");
        output.println("8 - Show evaluations");
        output.println("9 - Correct evaluation");
        output.println("10 - Cancel evaluation");
        output.println("11 - Show final grades");
        output.println("12 - Change grading rule");
        output.println("13 - Export final grades");
        output.println("14 - Run demo scenario");
        output.println("0 - Exit");
        output.print("Choose an action: ");
    }

    private void addStudent() {
        String fullName = readText("Full name: ");
        String group = readText("Group: ");
        Student student = facade.addStudent(fullName, group);
        output.println("Student added with id " + student.id());
    }

    private void removeStudent() {
        int studentId = readInt("Student id: ");
        facade.removeStudent(studentId);
        output.println("Student and the student's evaluations removed");
    }

    private void addCriterion() {
        String name = readText("Criterion name: ");
        double maxScore = readDouble("Max score: ");
        double weight = readDouble("Weight (from 0 to 1, e.g. 0.3): ");
        Criterion criterion = facade.addCriterion(name, maxScore, weight);
        output.println("Criterion added with id " + criterion.id());
    }

    private void removeCriterion() {
        int criterionId = readInt("Criterion id: ");
        facade.removeCriterion(criterionId);
        output.println("Criterion removed");
    }

    private void evaluate() {
        int studentId = readInt("Student id: ");
        int criterionId = readInt("Criterion id: ");
        double score = readDouble("Score: ");
        String comment = readText("Comment (can be empty): ");
        Evaluation evaluation = facade.evaluate(
                studentId,
                criterionId,
                score,
                comment
        );
        output.println("Evaluation saved with id " + evaluation.id());
    }

    private void correctEvaluation() {
        int evaluationId = readInt("Evaluation id: ");
        double score = readDouble("New score: ");
        String comment = readText("New comment (can be empty): ");
        facade.correctEvaluation(evaluationId, score, comment);
        output.println("Evaluation corrected");
    }

    private void cancelEvaluation() {
        int evaluationId = readInt("Evaluation id: ");
        facade.cancelEvaluation(evaluationId);
        output.println("Evaluation canceled");
    }

    private void changeRule() {
        List<String> names = facade.strategyNames();
        for (int i = 0; i < names.size(); i++) {
            output.println((i + 1) + " - " + names.get(i));
        }
        facade.selectStrategy(readInt("Choose a rule: "));
        output.println("Current rule: " + facade.currentStrategyName());
    }

    private void exportResults() {
        output.println("Formats: " + String.join(", ", facade.exportFormats()));
        String format = readText("Format [" + DEFAULT_FORMAT + "]: ");
        if (format.isEmpty()) {
            format = DEFAULT_FORMAT;
        }
        String fileName = readText(
                "File name [" + DEFAULT_FILE + "." + format + "]: "
        );
        if (fileName.isEmpty()) {
            fileName = DEFAULT_FILE + "." + format;
        }
        Path file = facade.exportResults(
                format,
                RESULTS_FOLDER + "/" + fileName
        );
        output.println("Results exported to " + file);
    }

    private String readText(final String prompt) {
        output.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new DomainValidationException("Input ended");
        }
        return scanner.nextLine().trim();
    }

    private int readInt(final String prompt) {
        return Integer.parseInt(readText(prompt));
    }

    // Accepts both 8.5 and 8,5
    private double readDouble(final String prompt) {
        return Double.parseDouble(readText(prompt).replace(',', '.'));
    }
}

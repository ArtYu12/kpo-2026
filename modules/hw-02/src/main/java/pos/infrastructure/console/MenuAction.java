package pos.infrastructure.console;

// Available menu actions
public enum MenuAction {
    ADD_STUDENT,
    SHOW_STUDENTS,
    REMOVE_STUDENT,
    ADD_CRITERION,
    SHOW_RUBRIC,
    REMOVE_CRITERION,
    EVALUATE,
    SHOW_EVALUATIONS,
    CORRECT_EVALUATION,
    CANCEL_EVALUATION,
    SHOW_RESULTS,
    CHANGE_RULE,
    EXPORT_RESULTS,
    RUN_DEMO,
    EXIT,
    UNKNOWN;

    // Finds an action by its menu number
    public static MenuAction fromCode(final String code) {
        return switch (code) {
            case "1" -> ADD_STUDENT;
            case "2" -> SHOW_STUDENTS;
            case "3" -> REMOVE_STUDENT;
            case "4" -> ADD_CRITERION;
            case "5" -> SHOW_RUBRIC;
            case "6" -> REMOVE_CRITERION;
            case "7" -> EVALUATE;
            case "8" -> SHOW_EVALUATIONS;
            case "9" -> CORRECT_EVALUATION;
            case "10" -> CANCEL_EVALUATION;
            case "11" -> SHOW_RESULTS;
            case "12" -> CHANGE_RULE;
            case "13" -> EXPORT_RESULTS;
            case "14" -> RUN_DEMO;
            case "0" -> EXIT;
            default -> UNKNOWN;
        };
    }
}

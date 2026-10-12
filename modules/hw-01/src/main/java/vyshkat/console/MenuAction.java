package vyshkat.console;

// Available menu actions
public enum MenuAction {
    ADD_TRANSPORT,
    ADD_EQUIPMENT,
    SHOW_TRANSPORT,
    SHOW_INVENTORY,
    SHOW_SUMMARY,
    SHOW_BEGINNER_TRANSPORT,
    EXIT,
    UNKNOWN;

    // Finds an action by its menu number
    public static MenuAction fromCode(final String code) {
        return switch (code) {
            case "1" -> ADD_TRANSPORT;
            case "2" -> ADD_EQUIPMENT;
            case "3" -> SHOW_TRANSPORT;
            case "4" -> SHOW_INVENTORY;
            case "5" -> SHOW_SUMMARY;
            case "6" -> SHOW_BEGINNER_TRANSPORT;
            case "0" -> EXIT;
            default -> UNKNOWN;
        };
    }
}

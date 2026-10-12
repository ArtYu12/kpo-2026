package vyshkat.console;

// Available transport types
public enum TransportType {
    SCOOTER,
    BICYCLE,
    ELECTRIC_BICYCLE,
    NEW_TRANSPORT,
    UNKNOWN;

    // Finds a transport type by its menu number
    public static TransportType fromCode(final String code) {
        return switch (code) {
            case "1" -> SCOOTER;
            case "2" -> BICYCLE;
            case "3" -> ELECTRIC_BICYCLE;
            case "4" -> NEW_TRANSPORT;
            default -> UNKNOWN;
        };
    }
}

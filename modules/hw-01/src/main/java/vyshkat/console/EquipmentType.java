package vyshkat.console;

// Available equipment types
public enum EquipmentType {
    HELMET,
    CHARGING_STATION,
    UNKNOWN;

    // Finds an equipment type by its menu number
    public static EquipmentType fromCode(final String code) {
        return switch (code) {
            case "1" -> HELMET;
            case "2" -> CHARGING_STATION;
            default -> UNKNOWN;
        };
    }
}

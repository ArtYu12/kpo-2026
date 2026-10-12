package vyshkat.model;

// An electric scooter
public final class Scooter extends ElectricTransport {

    // Creates a scooter
    public Scooter(
            final String inventoryNumber,
            final int beginnerEase,
            final boolean serviceable,
            final double dailyEnergyConsumption
    ) {
        super(
                "Scooter",
                inventoryNumber,
                beginnerEase,
                serviceable,
                dailyEnergyConsumption
        );
    }
}

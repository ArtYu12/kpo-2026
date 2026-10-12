package vyshkat.model;

// An electric bicycle
public final class ElectricBicycle extends ElectricTransport {

    // Creates an electric bicycle
    public ElectricBicycle(
            final String inventoryNumber,
            final int beginnerEase,
            final boolean serviceable,
            final double dailyEnergyConsumption
    ) {
        super(
                "Electric bike",
                inventoryNumber,
                beginnerEase,
                serviceable,
                dailyEnergyConsumption
        );
    }
}

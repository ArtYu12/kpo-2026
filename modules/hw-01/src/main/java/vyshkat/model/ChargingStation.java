package vyshkat.model;

import vyshkat.exception.DomainValidationException;

// A charging station
public final class ChargingStation extends Equipment
        implements EnergyConsumer {

    private final double dailyEnergyConsumption;

    // Creates a charging station
    public ChargingStation(
            final String inventoryNumber,
            final double energyConsumption
    ) {
        super("Charging station", inventoryNumber);
        if (!Double.isFinite(energyConsumption) || energyConsumption < 0) {
            throw new DomainValidationException(
                    "Daily energy expenditure cannot be negative"
            );
        }
        this.dailyEnergyConsumption = energyConsumption;
    }

    @Override
    public double dailyEnergyConsumption() {
        return dailyEnergyConsumption;
    }
}

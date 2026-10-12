package vyshkat.model;

import vyshkat.exception.DomainValidationException;

// Base electric transport
public abstract class ElectricTransport extends Transport
        implements EnergyConsumer {

    private final double dailyEnergyConsumption;

    // Creates electric transport
    protected ElectricTransport(
            final String name,
            final String inventoryNumber,
            final int beginnerEase,
            final boolean serviceable,
            final double energyConsumption
    ) {
        super(name, inventoryNumber, beginnerEase, serviceable);
        if (!Double.isFinite(energyConsumption) || energyConsumption < 0) {
            throw new DomainValidationException(
                    "Daily energy expenditure cannot be negative"
            );
        }
        this.dailyEnergyConsumption = energyConsumption;
    }

    @Override
    public final double dailyEnergyConsumption() {
        return dailyEnergyConsumption;
    }
}

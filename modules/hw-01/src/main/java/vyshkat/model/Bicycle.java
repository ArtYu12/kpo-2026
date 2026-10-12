package vyshkat.model;

// A rental bicycle
public final class Bicycle extends Transport {

    // Creates a bicycle
    public Bicycle(
            final String inventoryNumber,
            final int beginnerEase,
            final boolean serviceable
    ) {
        super("Bicycle", inventoryNumber, beginnerEase, serviceable);
    }
}

package vyshkat.service;

import vyshkat.model.Transport;

// Checks transport condition
@FunctionalInterface
public interface ServiceCenter {

    // Checks one transport item
    boolean passesInspection(Transport transport);
}

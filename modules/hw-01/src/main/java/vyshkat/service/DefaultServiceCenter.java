package vyshkat.service;

import org.springframework.stereotype.Service;
import vyshkat.model.Transport;

// Checks the service state
@Service
public final class DefaultServiceCenter implements ServiceCenter {

    @Override
    public boolean passesInspection(final Transport transport) {
        return transport != null && transport.serviceable();
    }
}

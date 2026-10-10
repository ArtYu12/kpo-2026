package studying.notification.factory;

import java.util.Map;
import studying.notification.exception.FactoryNotFoundException;
import studying.notification.exception.NotificationValidationException;
import studying.notification.model.NotificationChannel;

public final class NotificationFactoryRegistry {

    private final Map<NotificationChannel, NotificationFactory> factories;

    public NotificationFactoryRegistry(
            final Map<NotificationChannel, NotificationFactory>
                    registeredFactories
    ) {
        if (registeredFactories == null) {
            throw new NotificationValidationException(
                    "Набор фабрик обязателен"
            );
        }

        this.factories = Map.copyOf(registeredFactories);
    }

    public NotificationFactory get(
            final NotificationChannel channel
    ) {
        if (channel == null) {
            throw new NotificationValidationException(
                    "Канал уведомления обязателен"
            );
        }

        var factory = factories.get(channel);

        if (factory == null) {
            throw new FactoryNotFoundException(channel);
        }

        return factory;
    }
}
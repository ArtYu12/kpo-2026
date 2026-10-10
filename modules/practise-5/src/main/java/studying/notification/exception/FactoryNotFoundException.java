package studying.notification.exception;

import studying.notification.model.NotificationChannel;

public class FactoryNotFoundException extends RuntimeException {

    public FactoryNotFoundException(
            final NotificationChannel channel
    ) {
        super("Фабрика не зарегистрирована для канала: " + channel);
    }
}
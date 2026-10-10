package studying.notification.service;

import org.springframework.stereotype.Service;
import studying.notification.exception.NotificationValidationException;
import studying.notification.factory.NotificationFactoryRegistry;
import studying.notification.model.Notification;
import studying.notification.model.NotificationDraft;

@Service
public final class NotificationService {

    private final NotificationFactoryRegistry registry;

    public NotificationService(
            final NotificationFactoryRegistry factoryRegistry
    ) {
        if (factoryRegistry == null) {
            throw new NotificationValidationException(
                    "Реестр фабрик обязателен"
            );
        }

        this.registry = factoryRegistry;
    }

    public Notification create(final NotificationDraft draft) {
        if (draft == null) {
            throw new NotificationValidationException(
                    "Черновик уведомления обязателен"
            );
        }

        var factory = registry.get(draft.channel());

        return factory.create(draft);
    }
}

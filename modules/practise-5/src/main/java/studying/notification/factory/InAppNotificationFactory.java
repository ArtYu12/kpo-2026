package studying.notification.factory;

import studying.notification.exception.NotificationValidationException;
import studying.notification.model.Notification;
import studying.notification.model.NotificationChannel;
import studying.notification.model.NotificationDraft;

public final class InAppNotificationFactory
        implements NotificationFactory {

    @Override
    public Notification create(final NotificationDraft draft) {
        validateDraft(draft);

        return new InAppNotificationBuilder()
                .userId(draft.recipient())
                .text(draft.text())
                .createdAt(draft.createdAt())
                .priority(draft.priority())
                .build();
    }

    private static void validateDraft(final NotificationDraft draft) {
        if (draft == null) {
            throw new NotificationValidationException(
                    "Черновик уведомления обязателен"
            );
        }

        if (draft.channel() != NotificationChannel.IN_APP) {
            throw new NotificationValidationException(
                    "In-app фабрика поддерживает только канал IN_APP"
            );
        }
    }
}
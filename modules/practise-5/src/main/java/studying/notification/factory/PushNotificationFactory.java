package studying.notification.factory;

import studying.notification.exception.NotificationValidationException;
import studying.notification.model.Notification;
import studying.notification.model.NotificationChannel;
import studying.notification.model.NotificationDraft;

public final class PushNotificationFactory
        implements NotificationFactory {

    @Override
    public Notification create(final NotificationDraft draft) {
        validateDraft(draft);

        return new PushNotificationBuilder()
                .deviceId(draft.recipient())
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

        if (draft.channel() != NotificationChannel.PUSH) {
            throw new NotificationValidationException(
                    "Push-фабрика поддерживает только канал PUSH"
            );
        }
    }
}
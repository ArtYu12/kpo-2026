package studying.notification.factory;

import studying.notification.exception.NotificationValidationException;
import studying.notification.model.Notification;
import studying.notification.model.NotificationChannel;
import studying.notification.model.NotificationDraft;

public final class EmailNotificationFactory
        implements NotificationFactory {

    private static final String DEFAULT_SUBJECT =
            "Уведомление магазина";

    @Override
    public Notification create(final NotificationDraft draft) {
        validateDraft(draft);

        return new EmailNotificationBuilder()
                .emailAddress(draft.recipient())
                .subject(DEFAULT_SUBJECT)
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

        if (draft.channel() != NotificationChannel.EMAIL) {
            throw new NotificationValidationException(
                    "Email-фабрика поддерживает только канал EMAIL"
            );
        }
    }
}
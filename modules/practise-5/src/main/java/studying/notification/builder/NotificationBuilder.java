package studying.notification.builder;

import java.time.LocalDateTime;
import studying.notification.exception.NotificationValidationException;
import studying.notification.model.NotificationChannel;
import studying.notification.model.NotificationDraft;

public final class NotificationBuilder {

    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 5;

    private NotificationChannel channel;
    private String recipient;
    private String text;
    private LocalDateTime createdAt;
    private int priority;

    public NotificationBuilder channel(
            final NotificationChannel notificationChannel
    ) {
        this.channel = notificationChannel;
        return this;
    }

    public NotificationBuilder recipient(final String notificationRecipient) {
        this.recipient = notificationRecipient;
        return this;
    }

    public NotificationBuilder text(final String notificationText) {
        this.text = notificationText;
        return this;
    }

    public NotificationBuilder createdAt(
            final LocalDateTime notificationCreatedAt
    ) {
        this.createdAt = notificationCreatedAt;
        return this;
    }

    public NotificationBuilder priority(final int notificationPriority) {
        this.priority = notificationPriority;
        return this;
    }

    public NotificationDraft build() {
        validate();

        return new NotificationDraft(
                channel,
                recipient,
                text,
                createdAt,
                priority
        );
    }

    private void validate() {
        if (channel == null) {
            throw new NotificationValidationException(
                    "Канал уведомления обязателен"
            );
        }

        if (recipient == null || recipient.isBlank()) {
            throw new NotificationValidationException(
                    "Получатель уведомления обязателен"
            );
        }

        if (text == null || text.isBlank()) {
            throw new NotificationValidationException(
                    "Текст уведомления не должен быть пустым"
            );
        }

        if (createdAt == null) {
            throw new NotificationValidationException(
                    "Время создания уведомления обязательно"
            );
        }

        if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new NotificationValidationException(
                    "Приоритет должен находиться в диапазоне от 1 до 5"
            );
        }
    }
}
package studying.notification.factory;

import java.time.LocalDateTime;
import studying.notification.exception.NotificationValidationException;
import studying.notification.model.InAppNotification;

final class InAppNotificationBuilder {

    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 5;

    private String userId;
    private String text;
    private LocalDateTime createdAt;
    private int priority;

    InAppNotificationBuilder userId(final String identifier) {
        this.userId = identifier;
        return this;
    }

    InAppNotificationBuilder text(final String notificationText) {
        this.text = notificationText;
        return this;
    }

    InAppNotificationBuilder createdAt(final LocalDateTime creationTime) {
        this.createdAt = creationTime;
        return this;
    }

    InAppNotificationBuilder priority(final int notificationPriority) {
        this.priority = notificationPriority;
        return this;
    }

    InAppNotification build() {
        validate();

        return new InAppNotification(
                userId,
                text,
                createdAt,
                priority
        );
    }

    private void validate() {
        if (userId == null || userId.isBlank()) {
            throw new NotificationValidationException(
                    "Идентификатор пользователя обязателен"
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

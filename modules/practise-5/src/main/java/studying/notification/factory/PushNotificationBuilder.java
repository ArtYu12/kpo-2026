package studying.notification.factory;

import java.time.LocalDateTime;
import studying.notification.exception.NotificationValidationException;
import studying.notification.model.PushNotification;

final class PushNotificationBuilder {

    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 5;

    private String deviceId;
    private String text;
    private LocalDateTime createdAt;
    private int priority;

    PushNotificationBuilder deviceId(final String identifier) {
        this.deviceId = identifier;
        return this;
    }

    PushNotificationBuilder text(final String notificationText) {
        this.text = notificationText;
        return this;
    }

    PushNotificationBuilder createdAt(final LocalDateTime creationTime) {
        this.createdAt = creationTime;
        return this;
    }

    PushNotificationBuilder priority(final int notificationPriority) {
        this.priority = notificationPriority;
        return this;
    }

    PushNotification build() {
        validate();

        return new PushNotification(
                deviceId,
                text,
                createdAt,
                priority
        );
    }

    private void validate() {
        if (deviceId == null || deviceId.isBlank()) {
            throw new NotificationValidationException(
                    "Идентификатор устройства обязателен"
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
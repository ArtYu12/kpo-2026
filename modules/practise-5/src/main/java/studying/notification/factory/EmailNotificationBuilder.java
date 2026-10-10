package studying.notification.factory;

import java.time.LocalDateTime;
import studying.notification.exception.NotificationValidationException;
import studying.notification.model.EmailNotification;

final class EmailNotificationBuilder {

    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 5;

    private String emailAddress;
    private String subject;
    private String text;
    private LocalDateTime createdAt;
    private int priority;

    EmailNotificationBuilder emailAddress(final String address) {
        this.emailAddress = address;
        return this;
    }

    EmailNotificationBuilder subject(final String emailSubject) {
        this.subject = emailSubject;
        return this;
    }

    EmailNotificationBuilder text(final String notificationText) {
        this.text = notificationText;
        return this;
    }

    EmailNotificationBuilder createdAt(final LocalDateTime creationTime) {
        this.createdAt = creationTime;
        return this;
    }

    EmailNotificationBuilder priority(final int notificationPriority) {
        this.priority = notificationPriority;
        return this;
    }

    EmailNotification build() {
        validate();

        return new EmailNotification(
                emailAddress,
                subject,
                text,
                createdAt,
                priority
        );
    }

    private void validate() {
        if (emailAddress == null || emailAddress.isBlank()) {
            throw new NotificationValidationException(
                    "Адрес электронной почты обязателен"
            );
        }

        if (subject == null || subject.isBlank()) {
            throw new NotificationValidationException(
                    "Тема письма обязательна"
            );
        }

        validateCommonFields();
    }

    private void validateCommonFields() {
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
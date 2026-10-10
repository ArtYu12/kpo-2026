package studying.notification.factory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import studying.notification.model.EmailNotification;
import studying.notification.model.InAppNotification;
import studying.notification.model.Notification;
import studying.notification.model.NotificationChannel;
import studying.notification.model.NotificationDraft;
import studying.notification.model.PushNotification;

@DisplayName("Фабрики уведомлений")
class NotificationFactoryTest {

    private static final LocalDateTime CREATED_AT =
            LocalDateTime.of(2026, 10, 1, 10, 15);
    private static final String TEXT = "Заказ передан в доставку";
    private static final int PRIORITY = 5;

    @Test
    @DisplayName("Email-фабрика создаёт письмо с адресом и темой")
    void emailFactoryCreatesEmailNotification() {
        var draft = draft(
                NotificationChannel.EMAIL,
                "student@example.org"
        );

        var notification = new EmailNotificationFactory().create(draft);
        var email = assertInstanceOf(
                EmailNotification.class,
                notification
        );

        assertAll(
                () -> assertCommonFields(notification, draft),
                () -> assertEquals("student@example.org",
                        email.emailAddress()),
                () -> assertEquals("Уведомление магазина", email.subject())
        );
    }

    @Test
    @DisplayName("Push-фабрика создаёт уведомление с device ID")
    void pushFactoryCreatesPushNotification() {
        var draft = draft(NotificationChannel.PUSH, "device-42");

        var notification = new PushNotificationFactory().create(draft);
        var push = assertInstanceOf(
                PushNotification.class,
                notification
        );

        assertAll(
                () -> assertCommonFields(notification, draft),
                () -> assertEquals("device-42", push.deviceId())
        );
    }

    @Test
    @DisplayName("In-app фабрика создаёт уведомление с user ID")
    void inAppFactoryCreatesInAppNotification() {
        var draft = draft(NotificationChannel.IN_APP, "user-17");

        var notification = new InAppNotificationFactory().create(draft);
        var inApp = assertInstanceOf(
                InAppNotification.class,
                notification
        );

        assertAll(
                () -> assertCommonFields(notification, draft),
                () -> assertEquals("user-17", inApp.userId())
        );
    }

    private static NotificationDraft draft(
            final NotificationChannel channel,
            final String recipient
    ) {
        return new NotificationDraft(
                channel,
                recipient,
                TEXT,
                CREATED_AT,
                PRIORITY
        );
    }

    private static void assertCommonFields(
            final Notification notification,
            final NotificationDraft draft
    ) {
        assertAll(
                () -> assertEquals(draft.channel(), notification.channel()),
                () -> assertEquals(draft.recipient(),
                        notification.recipient()),
                () -> assertEquals(draft.text(), notification.text()),
                () -> assertEquals(draft.createdAt(),
                        notification.createdAt()),
                () -> assertEquals(draft.priority(), notification.priority())
        );
    }
}

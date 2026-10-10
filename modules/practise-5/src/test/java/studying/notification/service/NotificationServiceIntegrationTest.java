package studying.notification.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import studying.notification.builder.NotificationBuilder;
import studying.notification.factory.EmailNotificationFactory;
import studying.notification.factory.InAppNotificationFactory;
import studying.notification.factory.NotificationFactoryRegistry;
import studying.notification.factory.PushNotificationFactory;
import studying.notification.model.NotificationChannel;

@DisplayName("Полный сценарий создания уведомления")
class NotificationServiceIntegrationTest {

    private static final LocalDateTime CREATED_AT =
            LocalDateTime.of(2026, 10, 1, 10, 15);

    @Test
    @DisplayName("Черновик преобразуется в уведомление через реестр фабрик")
    void serviceCreatesNotificationThroughAbstractFactory() {
        var registry = new NotificationFactoryRegistry(
                Map.of(
                        NotificationChannel.EMAIL,
                        new EmailNotificationFactory(),
                        NotificationChannel.PUSH,
                        new PushNotificationFactory(),
                        NotificationChannel.IN_APP,
                        new InAppNotificationFactory()
                )
        );
        var service = new NotificationService(registry);
        var draft = new NotificationBuilder()
                .channel(NotificationChannel.PUSH)
                .recipient("device-42")
                .text("Заказ передан в доставку")
                .createdAt(CREATED_AT)
                .priority(4)
                .build();

        var notification = service.create(draft);

        assertAll(
                () -> assertEquals(NotificationChannel.PUSH,
                        notification.channel()),
                () -> assertEquals("device-42", notification.recipient()),
                () -> assertEquals("Заказ передан в доставку",
                        notification.text()),
                () -> assertEquals(CREATED_AT, notification.createdAt()),
                () -> assertEquals(4, notification.priority())
        );
    }
}

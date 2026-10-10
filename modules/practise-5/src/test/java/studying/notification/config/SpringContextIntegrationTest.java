package studying.notification.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import studying.notification.Main;
import studying.notification.service.NotificationService;

@SpringBootTest(classes = Main.class)
@DisplayName("Spring-контекст уведомлений")
class SpringContextIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("Контекст создаёт NotificationService со всеми зависимостями")
    void contextCreatesNotificationService() {
        assertNotNull(notificationService);
    }
}

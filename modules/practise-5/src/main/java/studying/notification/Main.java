package studying.notification;

import java.time.LocalDateTime;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import studying.notification.builder.NotificationBuilder;
import studying.notification.model.NotificationChannel;
import studying.notification.service.NotificationService;

@SpringBootApplication(proxyBeanMethods = false)
public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public ApplicationRunner notificationDemo(
            final NotificationService notificationService
    ) {
        return arguments -> {
            var draft = new NotificationBuilder()
                    .channel(NotificationChannel.EMAIL)
                    .recipient("student@example.org")
                    .text("Ваш заказ передан в доставку")
                    .createdAt(
                            LocalDateTime.of(2026, 10, 1, 10, 15)
                    )
                    .priority(5)
                    .build();

            var notification = notificationService.create(draft);

            System.out.printf(
                    "Канал: %s%nПолучатель: %s%nТекст: %s%n"
                            + "Время: %s%nПриоритет: %d%n",
                    notification.channel(),
                    notification.recipient(),
                    notification.text(),
                    notification.createdAt(),
                    notification.priority()
            );
        };
    }
}

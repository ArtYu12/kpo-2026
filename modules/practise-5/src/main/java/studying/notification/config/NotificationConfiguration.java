package studying.notification.config;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import studying.notification.factory.EmailNotificationFactory;
import studying.notification.factory.InAppNotificationFactory;
import studying.notification.factory.NotificationFactoryRegistry;
import studying.notification.factory.PushNotificationFactory;
import studying.notification.model.NotificationChannel;

@Configuration(proxyBeanMethods = false)
public final class NotificationConfiguration {

    @Bean
    public EmailNotificationFactory emailNotificationFactory() {
        return new EmailNotificationFactory();
    }

    @Bean
    public PushNotificationFactory pushNotificationFactory() {
        return new PushNotificationFactory();
    }

    @Bean
    public InAppNotificationFactory inAppNotificationFactory() {
        return new InAppNotificationFactory();
    }

    @Bean
    public NotificationFactoryRegistry notificationFactoryRegistry(
            final EmailNotificationFactory emailFactory,
            final PushNotificationFactory pushFactory,
            final InAppNotificationFactory inAppFactory
    ) {
        return new NotificationFactoryRegistry(
                Map.of(
                        NotificationChannel.EMAIL,
                        emailFactory,
                        NotificationChannel.PUSH,
                        pushFactory,
                        NotificationChannel.IN_APP,
                        inAppFactory
                )
        );
    }
}

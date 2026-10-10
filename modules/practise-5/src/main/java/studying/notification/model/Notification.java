package studying.notification.model;

import java.time.LocalDateTime;

public interface Notification {
    NotificationChannel channel();
    String recipient();
    String text();
    LocalDateTime createdAt();
    int priority();
}
package studying.notification.model;

import java.time.LocalDateTime;

public record InAppNotification(
        String userId,
        String text,
        LocalDateTime createdAt,
        int priority
) implements Notification {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.IN_APP;
    }

    @Override
    public String recipient() {
        return userId;
    }
}
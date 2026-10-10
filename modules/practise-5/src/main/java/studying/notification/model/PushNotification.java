package studying.notification.model;

import java.time.LocalDateTime;

public record PushNotification(
        String deviceId,
        String text,
        LocalDateTime createdAt,
        int priority
) implements Notification {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.PUSH;
    }

    @Override
    public String recipient() {
        return deviceId;
    }
}
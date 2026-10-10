package studying.notification.model;

import java.time.LocalDateTime;

public record EmailNotification(
        String emailAddress,
        String subject,
        String text,
        LocalDateTime createdAt,
        int priority
) implements Notification {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public String recipient() {
        return emailAddress;
    }
}
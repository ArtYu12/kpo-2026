package studying.notification.model;

import java.time.LocalDateTime;

public record NotificationDraft(
        NotificationChannel channel,
        String recipient,
        String text,
        LocalDateTime createdAt,
        int priority
) {
}
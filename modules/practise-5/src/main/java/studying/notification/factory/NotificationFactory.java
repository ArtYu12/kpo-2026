package studying.notification.factory;

import studying.notification.model.Notification;
import studying.notification.model.NotificationDraft;

public interface NotificationFactory {

    Notification create(NotificationDraft draft);
}
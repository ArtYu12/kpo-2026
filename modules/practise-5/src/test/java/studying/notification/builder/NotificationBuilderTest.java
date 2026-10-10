package studying.notification.builder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import studying.notification.exception.NotificationValidationException;
import studying.notification.model.NotificationChannel;

@DisplayName("Построение черновика уведомления")
class NotificationBuilderTest {

    private static final LocalDateTime CREATED_AT =
            LocalDateTime.of(2026, 10, 1, 10, 15);

    @Test
    @DisplayName("Builder переносит все переданные поля в черновик")
    void builderCreatesDraftWithAllFields() {
        var draft = validBuilder().build();

        assertAll(
                () -> assertEquals(NotificationChannel.EMAIL,
                        draft.channel()),
                () -> assertEquals("student@example.org",
                        draft.recipient()),
                () -> assertEquals("Заказ передан в доставку", draft.text()),
                () -> assertEquals(CREATED_AT, draft.createdAt()),
                () -> assertEquals(5, draft.priority())
        );
    }

    @ParameterizedTest(name = "Текст [{0}] отклоняется")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    @DisplayName("Builder отклоняет пустой текст")
    void builderRejectsBlankText(final String text) {
        var builder = validBuilder().text(text);

        assertThrows(
                NotificationValidationException.class,
                builder::build
        );
    }

    @Test
    @DisplayName("Builder отклоняет отсутствующего получателя")
    void builderRejectsMissingRecipient() {
        var builder = new NotificationBuilder()
                .channel(NotificationChannel.EMAIL)
                .text("Заказ передан в доставку")
                .createdAt(CREATED_AT)
                .priority(5);

        assertThrows(
                NotificationValidationException.class,
                builder::build
        );
    }

    @ParameterizedTest(name = "Приоритет {0} отклоняется")
    @ValueSource(ints = {-1, 0, 6, 10})
    @DisplayName("Builder отклоняет приоритет вне диапазона от 1 до 5")
    void builderRejectsInvalidPriority(final int priority) {
        var builder = validBuilder().priority(priority);

        assertThrows(
                NotificationValidationException.class,
                builder::build
        );
    }

    private static NotificationBuilder validBuilder() {
        return new NotificationBuilder()
                .channel(NotificationChannel.EMAIL)
                .recipient("student@example.org")
                .text("Заказ передан в доставку")
                .createdAt(CREATED_AT)
                .priority(5);
    }
}

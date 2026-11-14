package pos.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pos.domain.exception.DomainValidationException;

@DisplayName("Score")
class ScoreTest {

    @ParameterizedTest(name = "Score {0} of 10 is accepted")
    @ValueSource(doubles = {0, 5.5, 10})
    @DisplayName("Borders of the range are accepted")
    void acceptsScoreInRange(final double value) {
        var score = new Score(value, 10);

        assertEquals(value, score.value(), 0.0001);
    }

    @ParameterizedTest(name = "Score {0} of 10 is rejected")
    @ValueSource(doubles = {-1, -0.01, 10.01, 12})
    @DisplayName("Score can not leave the range")
    void rejectsScoreOutOfRange(final double value) {
        assertThrows(
                DomainValidationException.class,
                () -> new Score(value, 10)
        );
    }

    @Test
    @DisplayName("NaN is not a score")
    void rejectsNotANumber() {
        assertThrows(
                DomainValidationException.class,
                () -> new Score(Double.NaN, 10)
        );
    }

    @Test
    @DisplayName("Ratio is a part of the maximum")
    void calculatesRatio() {
        assertEquals(0.8, new Score(4, 5).ratio(), 0.0001);
    }
}

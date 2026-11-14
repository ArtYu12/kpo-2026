package pos.domain.model;

import java.time.Clock;
import java.time.LocalDate;

// Factory, the only place where evaluations are created
public final class EvaluationFactory {

    private final Clock clock;

    // Creates the factory
    public EvaluationFactory(final Clock clock) {
        this.clock = clock;
    }

    // Creates an evaluation for a criterion of the rubric
    public Evaluation create(
            final int id,
            final int studentId,
            final Rubric rubric,
            final int criterionId,
            final double value,
            final String comment
    ) {
        // throws if there is no such criterion
        Criterion criterion = rubric.criterion(criterionId);
        Score score = new Score(value, criterion.maxScore());
        return new Evaluation(
                id,
                studentId,
                criterionId,
                score,
                LocalDate.now(clock),
                comment
        );
    }
}

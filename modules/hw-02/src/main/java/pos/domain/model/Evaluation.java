package pos.domain.model;

import java.time.LocalDate;
import pos.domain.exception.DomainValidationException;

// Fact that a student got a score for a criterion
public final class Evaluation {

    private final int id;
    private final int studentId;
    private final int criterionId;
    private final LocalDate date;
    private Score score;
    private String comment;

    // Creates an evaluation, use EvaluationFactory instead
    Evaluation(
            final int id,
            final int studentId,
            final int criterionId,
            final Score score,
            final LocalDate date,
            final String comment
    ) {
        this.id = id;
        this.studentId = studentId;
        this.criterionId = criterionId;
        this.score = score;
        this.date = date;
        this.comment = normalizeComment(comment);
    }

    // Changes the score
    public void correct(
            final Criterion criterion,
            final double newValue,
            final String newComment
    ) {
        if (criterion.id() != criterionId) {
            throw new DomainValidationException(
                    "Evaluation " + id + " is not for criterion "
                            + criterion.id()
            );
        }
        score = new Score(newValue, criterion.maxScore());
        comment = normalizeComment(newComment);
    }

    // Returns evaluation identifier
    public int id() {
        return id;
    }

    // Returns evaluated student
    public int studentId() {
        return studentId;
    }

    // Returns evaluated criterion
    public int criterionId() {
        return criterionId;
    }

    // Returns given score
    public Score score() {
        return score;
    }

    // Returns date of the evaluation
    public LocalDate date() {
        return date;
    }

    // Returns comment, empty if there is no comment
    public String comment() {
        return comment;
    }

    // Evaluations are equal when they have the same id
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Evaluation evaluation)) {
            return false;
        }
        return id == evaluation.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    private static String normalizeComment(final String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }
}

package pos.domain.service;

import java.util.List;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.FinalGrade;
import pos.domain.model.Rubric;

// Weighted sum, missing criteria give 0
public final class WeightedSumStrategy implements GradingStrategy {

    @Override
    public String name() {
        return "Weighted sum (missing criteria give 0)";
    }

    @Override
    public FinalGrade calculate(
            final Rubric rubric,
            final List<Evaluation> evaluations
    ) {
        double sum = 0;
        for (Evaluation evaluation : evaluations) {
            Criterion criterion = rubric.criterion(evaluation.criterionId());
            sum += criterion.weight().value() * evaluation.score().ratio();
        }
        return new FinalGrade(sum * FinalGrade.MAX_GRADE);
    }
}

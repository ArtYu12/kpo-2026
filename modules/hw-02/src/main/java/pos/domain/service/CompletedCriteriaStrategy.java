package pos.domain.service;

import java.util.List;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.FinalGrade;
import pos.domain.model.Rubric;

// Current grade by evaluated criteria only
public final class CompletedCriteriaStrategy implements GradingStrategy {

    @Override
    public String name() {
        return "Current grade (only evaluated criteria)";
    }

    @Override
    public FinalGrade calculate(
            final Rubric rubric,
            final List<Evaluation> evaluations
    ) {
        double sum = 0;
        double usedWeight = 0;
        for (Evaluation evaluation : evaluations) {
            Criterion criterion = rubric.criterion(evaluation.criterionId());
            double weight = criterion.weight().value();
            sum += weight * evaluation.score().ratio();
            usedWeight += weight;
        }
        if (usedWeight == 0) {
            return new FinalGrade(0);
        }
        return new FinalGrade(sum / usedWeight * FinalGrade.MAX_GRADE);
    }
}

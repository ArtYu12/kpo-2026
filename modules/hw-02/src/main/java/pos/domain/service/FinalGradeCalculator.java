package pos.domain.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pos.domain.exception.DomainValidationException;
import pos.domain.model.Evaluation;
import pos.domain.model.FinalGrade;
import pos.domain.model.Rubric;
import pos.domain.model.Student;

// Domain service that calculates the final grade
public final class FinalGradeCalculator {

    // Calculates the final grade of a student by the chosen rule
    public FinalGrade calculate(
            final Student student,
            final Rubric rubric,
            final List<Evaluation> evaluations,
            final GradingStrategy strategy
    ) {
        checkConsistency(student, rubric, evaluations);
        return strategy.calculate(rubric, evaluations);
    }

    // Only this student's evaluations, one per criterion from the rubric
    private void checkConsistency(
            final Student student,
            final Rubric rubric,
            final List<Evaluation> evaluations
    ) {
        Set<Integer> usedCriteria = new HashSet<>();
        for (Evaluation evaluation : evaluations) {
            if (evaluation.studentId() != student.id()) {
                throw new DomainValidationException(
                        "Evaluation " + evaluation.id()
                                + " belongs to another student"
                );
            }
            if (!rubric.contains(evaluation.criterionId())) {
                throw new DomainValidationException(
                        "Criterion " + evaluation.criterionId()
                                + " is not in the rubric"
                );
            }
            if (!usedCriteria.add(evaluation.criterionId())) {
                throw new DomainValidationException(
                        "Criterion " + evaluation.criterionId()
                                + " is evaluated twice"
                );
            }
        }
    }
}

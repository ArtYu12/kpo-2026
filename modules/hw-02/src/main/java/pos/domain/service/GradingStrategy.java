package pos.domain.service;

import java.util.List;
import pos.domain.model.Evaluation;
import pos.domain.model.FinalGrade;
import pos.domain.model.Rubric;

// Strategy for calculating the final grade
public interface GradingStrategy {

    // Returns short name of the rule for the user
    String name();

    // Calculates the grade of one student
    FinalGrade calculate(Rubric rubric, List<Evaluation> evaluations);
}

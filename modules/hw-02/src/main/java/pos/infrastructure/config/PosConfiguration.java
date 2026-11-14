package pos.infrastructure.config;

import java.time.Clock;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pos.application.GradingFacade;
import pos.application.ResultExporter;
import pos.domain.model.EvaluationFactory;
import pos.domain.repository.EvaluationRepository;
import pos.domain.repository.RubricRepository;
import pos.domain.repository.StudentRepository;
import pos.domain.service.CompletedCriteriaStrategy;
import pos.domain.service.FinalGradeCalculator;
import pos.domain.service.WeightedSumStrategy;

// Registers domain and application classes as beans
@Configuration(proxyBeanMethods = false)
public final class PosConfiguration {

    // Returns system clock for evaluation dates
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    // Registers the evaluation factory
    @Bean
    public EvaluationFactory evaluationFactory(final Clock clock) {
        return new EvaluationFactory(clock);
    }

    // Returns domain service for the final grade
    @Bean
    public FinalGradeCalculator finalGradeCalculator() {
        return new FinalGradeCalculator();
    }

    // Registers the facade with all use cases
    @Bean
    public GradingFacade gradingFacade(
            final StudentRepository studentRepository,
            final RubricRepository rubricRepository,
            final EvaluationRepository evaluationRepository,
            final EvaluationFactory evaluationFactory,
            final FinalGradeCalculator gradeCalculator,
            final List<ResultExporter> exporters
    ) {
        return new GradingFacade(
                studentRepository,
                rubricRepository,
                evaluationRepository,
                evaluationFactory,
                gradeCalculator,
                List.of(
                        new WeightedSumStrategy(),
                        new CompletedCriteriaStrategy()
                ),
                exporters
        );
    }
}

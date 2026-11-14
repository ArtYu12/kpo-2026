package pos.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pos.PosApplication;
import pos.application.GradingFacade;
import pos.domain.repository.EvaluationRepository;
import pos.domain.repository.RubricRepository;
import pos.domain.repository.StudentRepository;

@SpringBootTest(classes = PosApplication.class)
@ActiveProfiles("test")
@DisplayName("Spring DI container")
class SpringContextTest {

    @Autowired
    private GradingFacade facade;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RubricRepository rubricRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    @DisplayName("Context creates the facade and the adapters")
    void contextCreatesBeans() {
        assertNotNull(facade);
        assertNotNull(studentRepository);
        assertNotNull(rubricRepository);
        assertNotNull(evaluationRepository);
        assertEquals(List.of("csv", "json"), sortedFormats());
    }

    private List<String> sortedFormats() {
        return facade.exportFormats().stream().sorted().toList();
    }
}

package pos.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import pos.domain.model.Rubric;
import pos.domain.repository.RubricRepository;

// Adapter that keeps the only rubric of the course in memory
@Repository
public final class InMemoryRubricRepository implements RubricRepository {

    private Rubric rubric = new Rubric();

    @Override
    public Rubric get() {
        return rubric;
    }

    @Override
    public void save(final Rubric changedRubric) {
        rubric = changedRubric;
    }
}

package pos.infrastructure.persistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import pos.domain.model.Evaluation;
import pos.domain.repository.EvaluationRepository;

// Adapter that keeps evaluations in memory
@Repository
public final class InMemoryEvaluationRepository
        implements EvaluationRepository {

    private final Map<Integer, Evaluation> evaluations =
            new LinkedHashMap<>();
    private int lastId = 0;

    @Override
    public int nextId() {
        lastId++;
        return lastId;
    }

    @Override
    public void save(final Evaluation evaluation) {
        evaluations.put(evaluation.id(), evaluation);
    }

    @Override
    public Optional<Evaluation> findById(final int id) {
        return Optional.ofNullable(evaluations.get(id));
    }

    @Override
    public List<Evaluation> findByStudent(final int studentId) {
        List<Evaluation> result = new ArrayList<>();
        for (Evaluation evaluation : evaluations.values()) {
            if (evaluation.studentId() == studentId) {
                result.add(evaluation);
            }
        }
        return result;
    }

    @Override
    public List<Evaluation> findAll() {
        return new ArrayList<>(evaluations.values());
    }

    @Override
    public void delete(final int id) {
        evaluations.remove(id);
    }
}

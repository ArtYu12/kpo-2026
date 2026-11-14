package pos.domain.repository;

import java.util.List;
import java.util.Optional;
import pos.domain.model.Evaluation;

// Port for storing evaluations outside the domain
public interface EvaluationRepository {

    // Returns identifier for a new evaluation
    int nextId();

    // Saves a new or changed evaluation
    void save(Evaluation evaluation);

    // Finds an evaluation by id
    Optional<Evaluation> findById(int id);

    // Finds all evaluations of a student
    List<Evaluation> findByStudent(int studentId);

    // Returns all evaluations
    List<Evaluation> findAll();

    // Deletes an evaluation
    void delete(int id);
}

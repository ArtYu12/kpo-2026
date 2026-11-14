package pos.domain.repository;

import pos.domain.model.Rubric;

// Port for storing the rubric outside the domain
public interface RubricRepository {

    // Returns the rubric of the course
    Rubric get();

    // Saves the changed rubric
    void save(Rubric rubric);
}

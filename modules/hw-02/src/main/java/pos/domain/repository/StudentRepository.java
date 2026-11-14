package pos.domain.repository;

import java.util.List;
import java.util.Optional;
import pos.domain.model.Student;

// Port for storing students outside the domain
public interface StudentRepository {

    // Returns identifier for a new student
    int nextId();

    // Saves a new or changed student
    void save(Student student);

    // Finds a student by id
    Optional<Student> findById(int id);

    // Returns all students in order of adding
    List<Student> findAll();

    // Deletes a student
    void delete(int id);
}

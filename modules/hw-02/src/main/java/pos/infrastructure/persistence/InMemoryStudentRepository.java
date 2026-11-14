package pos.infrastructure.persistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import pos.domain.model.Student;
import pos.domain.repository.StudentRepository;

// Adapter that keeps students in memory
@Repository
public final class InMemoryStudentRepository implements StudentRepository {

    private final Map<Integer, Student> students = new LinkedHashMap<>();
    private int lastId = 0;

    @Override
    public int nextId() {
        lastId++;
        return lastId;
    }

    @Override
    public void save(final Student student) {
        students.put(student.id(), student);
    }

    @Override
    public Optional<Student> findById(final int id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public void delete(final int id) {
        students.remove(id);
    }
}

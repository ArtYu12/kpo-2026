package pos.domain.model;

// A student who is evaluated on the course
public final class Student {

    private final int id;
    private final String fullName;
    private final String group;

    // Creates a student
    public Student(final int id, final String fullName, final String group) {
        DomainChecks.requireText(fullName, "Student name required");
        DomainChecks.requireText(group, "Group required");
        this.id = id;
        this.fullName = fullName.trim();
        this.group = group.trim();
    }

    // Returns student identifier
    public int id() {
        return id;
    }

    // Returns full name of the student
    public String fullName() {
        return fullName;
    }

    // Returns academic group
    public String group() {
        return group;
    }

    // Students are equal when they have the same id
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Student student)) {
            return false;
        }
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

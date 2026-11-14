package pos.application;

// Final result of a student
public record StudentResult(
        int studentId,
        String fullName,
        String group,
        double grade,
        int roundedGrade,
        String rule
) { }

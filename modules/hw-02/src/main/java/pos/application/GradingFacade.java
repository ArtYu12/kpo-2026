package pos.application;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import pos.domain.exception.DomainValidationException;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.EvaluationFactory;
import pos.domain.model.FinalGrade;
import pos.domain.model.Rubric;
import pos.domain.model.Student;
import pos.domain.repository.EvaluationRepository;
import pos.domain.repository.RubricRepository;
import pos.domain.repository.StudentRepository;
import pos.domain.service.FinalGradeCalculator;
import pos.domain.service.GradingStrategy;

// Facade with all use cases for the console
public final class GradingFacade {

    private final StudentRepository studentRepository;
    private final RubricRepository rubricRepository;
    private final EvaluationRepository evaluationRepository;
    private final EvaluationFactory evaluationFactory;
    private final FinalGradeCalculator gradeCalculator;
    private final List<GradingStrategy> strategies;
    private final List<ResultExporter> exporters;
    private GradingStrategy currentStrategy;

    // Creates the facade
    public GradingFacade(
            final StudentRepository studentRepository,
            final RubricRepository rubricRepository,
            final EvaluationRepository evaluationRepository,
            final EvaluationFactory evaluationFactory,
            final FinalGradeCalculator gradeCalculator,
            final List<GradingStrategy> strategies,
            final List<ResultExporter> exporters
    ) {
        this.studentRepository = studentRepository;
        this.rubricRepository = rubricRepository;
        this.evaluationRepository = evaluationRepository;
        this.evaluationFactory = evaluationFactory;
        this.gradeCalculator = gradeCalculator;
        this.strategies = List.copyOf(strategies);
        this.exporters = List.copyOf(exporters);
        this.currentStrategy = this.strategies.get(0);
    }

    // Adds a student
    public Student addStudent(final String fullName, final String group) {
        Student student = new Student(
                studentRepository.nextId(),
                fullName,
                group
        );
        studentRepository.save(student);
        return student;
    }

    // Returns all students
    public List<Student> students() {
        return studentRepository.findAll();
    }

    // Finds a student
    public Student student(final int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new DomainValidationException(
                        "Student " + studentId + " not found"
                ));
    }

    // Removes a student and the student's evaluations
    public void removeStudent(final int studentId) {
        Student student = student(studentId);
        for (Evaluation evaluation : evaluationRepository
                .findByStudent(student.id())) {
            evaluationRepository.delete(evaluation.id());
        }
        studentRepository.delete(student.id());
    }

    // Adds a criterion to the rubric
    public Criterion addCriterion(
            final String name,
            final double maxScore,
            final double weight
    ) {
        Rubric rubric = rubricRepository.get();
        Criterion criterion = rubric.addCriterion(name, maxScore, weight);
        rubricRepository.save(rubric);
        return criterion;
    }

    // Returns all criteria of the rubric
    public List<Criterion> criteria() {
        return rubricRepository.get().criteria();
    }

    // Finds a criterion
    public Criterion criterion(final int criterionId) {
        return rubricRepository.get().criterion(criterionId);
    }

    // Returns sum of weights of the rubric
    public double totalWeight() {
        return rubricRepository.get().totalWeight();
    }

    // Removes a criterion that has no evaluations
    public void removeCriterion(final int criterionId) {
        Rubric rubric = rubricRepository.get();
        rubric.criterion(criterionId);
        for (Evaluation evaluation : evaluationRepository.findAll()) {
            if (evaluation.criterionId() == criterionId) {
                throw new DomainValidationException(
                        "Criterion " + criterionId
                                + " has evaluations, cancel them first"
                );
            }
        }
        rubric.removeCriterion(criterionId);
        rubricRepository.save(rubric);
    }

    // Gives a score to a student for a criterion
    public Evaluation evaluate(
            final int studentId,
            final int criterionId,
            final double score,
            final String comment
    ) {
        Student student = student(studentId);
        for (Evaluation evaluation : evaluationRepository
                .findByStudent(student.id())) {
            if (evaluation.criterionId() == criterionId) {
                throw new DomainValidationException(
                        "Student already has evaluation " + evaluation.id()
                                + " for this criterion, correct it instead"
                );
            }
        }
        Evaluation evaluation = evaluationFactory.create(
                evaluationRepository.nextId(),
                student.id(),
                rubricRepository.get(),
                criterionId,
                score,
                comment
        );
        evaluationRepository.save(evaluation);
        return evaluation;
    }

    // Returns all evaluations
    public List<Evaluation> evaluations() {
        return evaluationRepository.findAll();
    }

    // Corrects the score of an evaluation
    public void correctEvaluation(
            final int evaluationId,
            final double score,
            final String comment
    ) {
        Evaluation evaluation = findEvaluation(evaluationId);
        Criterion criterion = criterion(evaluation.criterionId());
        evaluation.correct(criterion, score, comment);
        evaluationRepository.save(evaluation);
    }

    // Cancels an evaluation
    public void cancelEvaluation(final int evaluationId) {
        Evaluation evaluation = findEvaluation(evaluationId);
        evaluationRepository.delete(evaluation.id());
    }

    // Returns final results of all students
    public List<StudentResult> results() {
        Rubric rubric = rubricRepository.get();
        List<StudentResult> results = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            FinalGrade grade = gradeCalculator.calculate(
                    student,
                    rubric,
                    evaluationRepository.findByStudent(student.id()),
                    currentStrategy
            );
            results.add(new StudentResult(
                    student.id(),
                    student.fullName(),
                    student.group(),
                    grade.value(),
                    grade.rounded(),
                    currentStrategy.name()
            ));
        }
        return results;
    }

    // Returns names of available grading rules
    public List<String> strategyNames() {
        List<String> names = new ArrayList<>();
        for (GradingStrategy strategy : strategies) {
            names.add(strategy.name());
        }
        return names;
    }

    // Returns name of the current grading rule
    public String currentStrategyName() {
        return currentStrategy.name();
    }

    // Changes the grading rule
    public void selectStrategy(final int number) {
        if (number < 1 || number > strategies.size()) {
            throw new DomainValidationException(
                    "Unknown grading rule: " + number
            );
        }
        currentStrategy = strategies.get(number - 1);
    }

    // Returns supported export formats
    public List<String> exportFormats() {
        List<String> formats = new ArrayList<>();
        for (ResultExporter exporter : exporters) {
            formats.add(exporter.format());
        }
        return formats;
    }

    // Exports final results to a file
    public Path exportResults(final String format, final String fileName) {
        for (ResultExporter exporter : exporters) {
            if (exporter.format().equalsIgnoreCase(format)) {
                Path file = Path.of(fileName.trim()).toAbsolutePath();
                exporter.export(results(), file);
                return file;
            }
        }
        throw new DomainValidationException(
                "Unknown export format: " + format
        );
    }

    private Evaluation findEvaluation(final int evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new DomainValidationException(
                        "Evaluation " + evaluationId + " not found"
                ));
    }
}

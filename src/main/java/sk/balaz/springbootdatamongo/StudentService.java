package sk.balaz.springbootdatamongo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional.get();
        }
        throw new ResourceNotFoundException(String.format("Student ID [%s] not found", id));
    }

    public Student createStudent(StudentRequest request) {

        Student student = new Student(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.gender(),
                request.address(),
                request.favouriteSubjects(),
                request.totalSpentInBooks(),
                request.created()
        );
        return studentRepository.save(student);
    }

    public Student updateStudent(String id, StudentRequest request) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Student ID [%s] not found", id));
        }
        Student student = studentOptional.get();
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setGender(request.gender());
        student.setAddress(request.address());
        student.setFavouriteSubjects(request.favouriteSubjects());
        student.setCreated(request.created());

        return studentRepository.save(student);
    }
}

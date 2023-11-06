package sk.balaz.springbootdatamongo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    public static final String STUDENT_ID_S_NOT_FOUND = "Student ID [%s] not found";

    private final StudentRepository studentRepository;

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional.get();
        }
        throw new ResourceNotFoundException(String.format(STUDENT_ID_S_NOT_FOUND, id));
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
            throw new ResourceNotFoundException(String.format(STUDENT_ID_S_NOT_FOUND, id));
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

    public void deleteStudentById(String id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(STUDENT_ID_S_NOT_FOUND, id));
        }
        studentRepository.deleteById(id);
    }
}

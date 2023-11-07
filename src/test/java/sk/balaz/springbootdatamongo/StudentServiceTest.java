package sk.balaz.springbootdatamongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Captor
    ArgumentCaptor<Student> studentArgumentCaptor;

    private StudentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    @Test
    void itShouldGetAllStudents() {

        Address address = new Address(
                "England",
                "London",
                "NE9"
        );
        String email = "tomas@tomas.edu";
        Student student = new Student(
                "Tomas ",
                "Tomas",
                email,
                Gender.MALE,
                address,
                List.of("Computer Science", "Maths"),
                BigDecimal.TEN,
                LocalDateTime.now()
        );

        given(studentRepository.findAll()).willReturn(List.of(student));

        List<Student> students = underTest.getStudents();

        assertThat(students).isNotEmpty();
    }

    @Test
    void itShouldGetStudentById() {

        String id = UUID.randomUUID().toString();
        given(studentRepository.findById(id))
                .willReturn(Optional.of(
                        new Student(
                                "Tomas",
                                "Tomas",
                                "tomas@tomas.edu",
                                Gender.MALE,
                                null,
                                List.of("Computer Science", "Maths"),
                                BigDecimal.TEN,
                                LocalDateTime.now()
                        )
                ));

        Student student = underTest.getStudentById(id);

        assertThat(student.getFirstName()).isEqualTo("Tomas");
        assertThat(student.getLastName()).isEqualTo("Tomas");
    }

    @Test
    void itShouldThrowWhenStudentIdNotFound() {

        String id = UUID.randomUUID().toString();

        assertThatThrownBy(() -> underTest.getStudentById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Student ID [%s] not found", id));
    }

    @Test
    void itShouldCreateStudent() {

        StudentRequest request = new StudentRequest(
                "Tomas",
                "Tomas",
                "tomas@tomas.edu",
                Gender.MALE,
                null,
                List.of("Computer Science", "Maths"),
                BigDecimal.TEN,
                LocalDateTime.now()
        );

        underTest.createStudent(request);

        then(studentRepository).should().save(studentArgumentCaptor.capture());
        Student studentArgumentCaptorValue = studentArgumentCaptor.getValue();
        assertThat(studentArgumentCaptorValue.getFirstName()).isEqualTo(request.firstName());
    }

    @Test
    void itShouldUpdateStudent() {

        String id = UUID.randomUUID().toString();
        StudentRequest request = new StudentRequest(
                "Tomas",
                "Tomass",
                "tomas@tomas.edu",
                Gender.MALE,
                null,
                List.of("Computer Science", "Maths"),
                BigDecimal.TEN,
                LocalDateTime.now()
        );

        Student student = new Student(
                "Tomas",
                "Tomas",
                "tomas@tomas.edu",
                Gender.MALE,
                null,
                List.of("Computer Science", "Maths"),
                BigDecimal.TEN,
                LocalDateTime.now()
        );
        given(studentRepository.findById(id))
                .willReturn(Optional.of(student));
        given(studentRepository.save(any(Student.class)))
                .willReturn(student);

        Student s = underTest.updateStudent(id, request);

        assertThat(s.getLastName()).isEqualTo("Tomass");

    }

    @Test
    void itShouldThrowWhenStudentIdInUpdateNotFound() {

        String id = UUID.randomUUID().toString();
        StudentRequest request = new StudentRequest(
                "Tomas",
                "Tomass",
                "tomas@tomas.edu",
                Gender.MALE,
                null,
                List.of("Computer Science", "Maths"),
                BigDecimal.TEN,
                LocalDateTime.now()
        );

        assertThatThrownBy(() -> underTest.updateStudent(id, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Student ID [%s] not found", id));

        then(studentRepository).should().findById(id);
        then(studentRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldDeleteStudent() {

        String id = UUID.randomUUID().toString();
        Student student = new Student(
                "Tomas",
                "Tomas",
                "tomas@tomas.edu",
                Gender.MALE,
                null,
                List.of("Computer Science", "Maths"),
                BigDecimal.TEN,
                LocalDateTime.now()
        );

        given(studentRepository.findById(id))
                .willReturn(Optional.of(student));

        underTest.deleteStudentById(id);

        then(studentRepository).should(times(1)).deleteById(id);
    }

    @Test
    void itShouldThrowWhenStudentIdInDeleteNotFound() {

        String id = UUID.randomUUID().toString();

        assertThatThrownBy(() -> underTest.deleteStudentById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Student ID [%s] not found", id));

        then(studentRepository).should().findById(id);
        then(studentRepository).shouldHaveNoMoreInteractions();
    }
}
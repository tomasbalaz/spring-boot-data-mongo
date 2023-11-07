package sk.balaz.springbootdatamongo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void itShouldFindStudentByEmail() {

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

        underTest.save(student);

        Optional<Student> optionalStudent = underTest.findStudentByEmail(email);
        assertThat(optionalStudent.get())
                .usingRecursiveComparison()
                .isEqualTo(student);
    }
}
package sk.balaz.springbootdatamongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(MongoTemplate template, StudentRepository studentRepository) {
        return args -> {
            Address address = new Address(
                    "England",
                    "London",
                    "NE9"
            );
            String mail = "tomas@tomas.edu";
            Student student = new Student(
                    "Tomas ",
                    "Tomas",
                    mail,
                    Gender.MALE,
                    address,
                    List.of("Computer Science", "Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

            Query query = new Query();
            query.addCriteria(Criteria.where("email").is("tomas@tomas.edu"));

            List<Student> students = template.find(query, Student.class);

            if (students.size() > 1) {
                throw new IllegalStateException(String.format("found many students with email [%s]", mail));
            }

            if (students.isEmpty()) {
                System.out.println("Inserting student " + student);
                studentRepository.save(student);
            }
            else {
                System.out.println(student + " already exists");
            }

        };
    }

}

package sk.balaz.springbootdatamongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldGetAllStudents() throws Exception {

        mockMvc.perform(get("/api/v1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void itShouldGetStudentById() throws Exception {

        mockMvc.perform(get("/api/v1/students/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void itShouldCreateStudent() throws Exception {

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

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(asJsonString(request))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void itShouldUpdateStudent() throws Exception {

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

        mockMvc.perform(put("/api/v1/students/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(asJsonString(request))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void itShouldDeleteStudent() throws Exception {

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

        mockMvc.perform(delete("/api/v1/students/{id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(asJsonString(request))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed convert object to json");
            return null;
        }
    }
}
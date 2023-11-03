package sk.balaz.springbootdatamongo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StudentRequest(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        Address address,
        List<String> favouriteSubjects,
        BigDecimal totalSpentInBooks,
        LocalDateTime created

) {
}

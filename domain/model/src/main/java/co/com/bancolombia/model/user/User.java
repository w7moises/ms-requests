package co.com.bancolombia.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private LocalDate birthDate;
    private String address;
    private String cellphone;
    private String email;
    private BigDecimal salary;
}

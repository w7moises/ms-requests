package co.com.bancolombia.api.dto.user;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String documentNumber;
    private String name;
    private String lastName;
    private String email;
    private BigDecimal salary;
}

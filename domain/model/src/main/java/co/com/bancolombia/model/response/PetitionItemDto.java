package co.com.bancolombia.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetitionItemDto {
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String documentNumber;
    private String loanPetitionType;
    private BigDecimal interestRate;
    private String loanPetitionState;
}

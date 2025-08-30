package co.com.bancolombia.api.dto.loanpetition;

import lombok.*;

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

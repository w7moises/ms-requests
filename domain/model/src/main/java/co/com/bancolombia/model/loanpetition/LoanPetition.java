package co.com.bancolombia.model.loanpetition;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanPetition {
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String documentNumber;
    private Long stateId;
    private Long loanTypeId;
}

package co.com.bancolombia.api.dto.loanpetition;

import java.math.BigDecimal;

public record EditLoanPetitionDto(
        BigDecimal amount,
        Integer term,
        String email,
        String documentNumber,
        Long stateId,
        Long loanTypeId
) {
}

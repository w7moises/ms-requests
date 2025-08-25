package co.com.bancolombia.api.dto.loanpetition;

import java.math.BigDecimal;

public record EditLoanPetitionDto(
        BigDecimal amount,
        Integer term,
        String documentNumber
) {
}

package co.com.bancolombia.api.dto.loanpetition;

import java.math.BigDecimal;

public record CreateLoanPetitionDto(
        BigDecimal amount,
        Integer term,
        String documentNumber
) {
}

package co.com.bancolombia.api.dto.loanpetition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EditLoanPetitionDto(
        @NotNull(message = "{amount.required}")
        BigDecimal amount,
        @NotNull(message = "{term.required}")
        Integer term,
        @NotBlank(message = "{document.required}")
        String documentNumber
) {
}

package co.com.bancolombia.api.dto.loanpetition;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateLoanPetitionDto(
        @NotNull(message = "{amount.required}")
        @DecimalMax(value = "999999999", inclusive = false, message = "{amount.max}")
        @DecimalMin(value = "0", inclusive = false, message = "{amount.max}")
        BigDecimal amount,
        @NotNull(message = "{term.required}")
        Integer term,
        @NotBlank(message = "{document.required}")
        String documentNumber
) {
}

package co.com.bancolombia.api.dto.loantype;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateLoanTypeDto(
        @NotBlank(message = "{name.required}")
        String name,

        @NotNull(message = "{min.amount.required}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{amount.min}")
        BigDecimal minAmount,

        @NotNull(message = "{max.amount.required}")
        @DecimalMax(value = "999999999", inclusive = false, message = "{amount.max}")
        BigDecimal maxAmount,

        @NotNull(message = "{interest.required}")
        BigDecimal interestRate,

        Boolean automaticValidation
) {
}

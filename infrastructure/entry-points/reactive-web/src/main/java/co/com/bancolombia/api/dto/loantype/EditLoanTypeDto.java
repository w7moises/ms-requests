package co.com.bancolombia.api.dto.loantype;

import java.math.BigDecimal;

public record EditLoanTypeDto(
        String name,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Boolean automaticValidation
) {
}

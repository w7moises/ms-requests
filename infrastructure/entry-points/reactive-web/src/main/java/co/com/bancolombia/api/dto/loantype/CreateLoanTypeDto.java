package co.com.bancolombia.api.dto.loantype;

import java.math.BigDecimal;

public record CreateLoanTypeDto(
        String name,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Boolean automaticValidation
) {
}

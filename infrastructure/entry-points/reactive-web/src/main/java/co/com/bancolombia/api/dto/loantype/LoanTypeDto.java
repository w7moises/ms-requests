package co.com.bancolombia.api.dto.loantype;

import java.math.BigDecimal;

public record LoanTypeDto(
        Long id,
        String name,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Boolean automaticValidation
) {
}

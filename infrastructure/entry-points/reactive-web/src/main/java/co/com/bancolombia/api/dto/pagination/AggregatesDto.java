package co.com.bancolombia.api.dto.pagination;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregatesDto {
    private BigDecimal totalMonthlyDebtAmountApproved;
}

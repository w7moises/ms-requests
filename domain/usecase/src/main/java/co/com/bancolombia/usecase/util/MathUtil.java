package co.com.bancolombia.usecase.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathUtil {
    private static final int SCALE = 12;
    private static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);

    public static BigDecimal monthlyDebtCalculation(BigDecimal principal, BigDecimal monthlyRate, int months) {
        monthlyRate = monthlyRate.divide(BigDecimal.valueOf(100), MATH_CONTEXT);
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(months), SCALE, RoundingMode.HALF_UP);
        }
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate, MATH_CONTEXT);
        BigDecimal pow = onePlusRate.pow(months, MATH_CONTEXT);
        BigDecimal denominator = BigDecimal.ONE
                .subtract(BigDecimal.ONE.divide(pow, SCALE, RoundingMode.HALF_UP), MATH_CONTEXT);
        return principal.multiply(monthlyRate, MATH_CONTEXT)
                .divide(denominator, SCALE, RoundingMode.HALF_UP);
    }
}

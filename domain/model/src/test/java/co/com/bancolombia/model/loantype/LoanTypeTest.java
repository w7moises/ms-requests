package co.com.bancolombia.model.loantype;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanTypeTest {
    private static final LoanType LOAN_MICRO = LoanType.builder()
            .id(1L)
            .name("MICRO")
            .minAmount(new BigDecimal("100.00"))
            .maxAmount(new BigDecimal("1000.00"))
            .interestRate(new BigDecimal("0.12"))
            .automaticValidation(Boolean.TRUE)
            .build();

    private static final LoanType LOAN_PERSONAL = LoanType.builder()
            .id(2L)
            .name("PERSONAL")
            .minAmount(new BigDecimal("500.00"))
            .maxAmount(new BigDecimal("5000.00"))
            .interestRate(new BigDecimal("0.18"))
            .automaticValidation(Boolean.FALSE)
            .build();

    @Test
    void shouldBuild() {
        assertEquals(1L, LOAN_MICRO.getId());
        assertEquals("MICRO", LOAN_MICRO.getName());
        assertEquals(new BigDecimal("100.00"), LOAN_MICRO.getMinAmount());
        assertEquals(new BigDecimal("1000.00"), LOAN_MICRO.getMaxAmount());
        assertEquals(new BigDecimal("0.12"), LOAN_MICRO.getInterestRate());
        assertEquals(Boolean.TRUE, LOAN_MICRO.getAutomaticValidation());
    }

    @Test
    void settersAndGetters() {
        LoanType lt = new LoanType();
        lt.setId(LOAN_PERSONAL.getId());
        lt.setName(LOAN_PERSONAL.getName());
        lt.setMinAmount(LOAN_PERSONAL.getMinAmount());
        lt.setMaxAmount(LOAN_PERSONAL.getMaxAmount());
        lt.setInterestRate(LOAN_PERSONAL.getInterestRate());
        lt.setAutomaticValidation(LOAN_PERSONAL.getAutomaticValidation());

        assertEquals(2L, lt.getId());
        assertEquals("PERSONAL", lt.getName());
        assertEquals(new BigDecimal("500.00"), lt.getMinAmount());
        assertEquals(new BigDecimal("5000.00"), lt.getMaxAmount());
        assertEquals(new BigDecimal("0.18"), lt.getInterestRate());
        assertEquals(Boolean.FALSE, lt.getAutomaticValidation());
    }

    @Test
    void builder() {
        LoanType mod = LOAN_MICRO.toBuilder()
                .interestRate(new BigDecimal("0.15"))
                .automaticValidation(Boolean.FALSE)
                .build();
        assertEquals(1L, mod.getId());
        assertEquals("MICRO", mod.getName());
        assertEquals(new BigDecimal("0.15"), mod.getInterestRate());
        assertEquals(Boolean.FALSE, mod.getAutomaticValidation());
    }
}

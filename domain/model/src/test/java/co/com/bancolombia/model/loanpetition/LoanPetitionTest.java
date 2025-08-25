package co.com.bancolombia.model.loanpetition;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanPetitionTest {
    private static final LoanPetition PETITION_1 = LoanPetition.builder()
            .id(1L)
            .amount(new BigDecimal("1500.00"))
            .term(12)
            .email("test@gmail.com")
            .documentNumber("73727173")
            .stateId(100L)
            .loanTypeId(10L)
            .build();

    private static final LoanPetition PETITION_2 = LoanPetition.builder()
            .id(2L)
            .amount(new BigDecimal("5000.00"))
            .term(24)
            .email("test2@gmail.com")
            .documentNumber("12345678")
            .stateId(101L)
            .loanTypeId(11L)
            .build();

    @Test
    void shouldBuild() {
        assertEquals(1L, PETITION_1.getId());
        assertEquals(new BigDecimal("1500.00"), PETITION_1.getAmount());
        assertEquals(12, PETITION_1.getTerm());
        assertEquals("test@gmail.com", PETITION_1.getEmail());
        assertEquals("73727173", PETITION_1.getDocumentNumber());
        assertEquals(100L, PETITION_1.getStateId());
        assertEquals(10L, PETITION_1.getLoanTypeId());
    }

    @Test
    void settersAndGetters() {
        LoanPetition p = new LoanPetition();
        p.setId(PETITION_2.getId());
        p.setAmount(PETITION_2.getAmount());
        p.setTerm(PETITION_2.getTerm());
        p.setEmail(PETITION_2.getEmail());
        p.setDocumentNumber(PETITION_2.getDocumentNumber());
        p.setStateId(PETITION_2.getStateId());
        p.setLoanTypeId(PETITION_2.getLoanTypeId());

        assertEquals(2L, p.getId());
        assertEquals(new BigDecimal("5000.00"), p.getAmount());
        assertEquals(24, p.getTerm());
        assertEquals("test2@gmail.com", p.getEmail());
        assertEquals("12345678", p.getDocumentNumber());
        assertEquals(101L, p.getStateId());
        assertEquals(11L, p.getLoanTypeId());
    }

    @Test
    void builder() {
        LoanPetition p = PETITION_1.toBuilder()
                .amount(new BigDecimal("2000.00"))
                .term(18)
                .build();
        assertEquals(1L, p.getId());
        assertEquals(new BigDecimal("2000.00"), p.getAmount());
        assertEquals(18, p.getTerm());
        assertEquals("test@gmail.com", p.getEmail());
    }
}

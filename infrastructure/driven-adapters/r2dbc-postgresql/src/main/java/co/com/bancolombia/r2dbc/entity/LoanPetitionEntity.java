package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("loan_petitions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanPetitionEntity {
    @Id
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private String email;
    @Column("document_number")
    private String documentNumber;
    @Column("state_id")
    private Long stateId;
    @Column("loan_type_id")
    private Long loanTypeId;
}

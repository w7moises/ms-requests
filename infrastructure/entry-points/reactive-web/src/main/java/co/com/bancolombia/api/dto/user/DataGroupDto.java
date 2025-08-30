package co.com.bancolombia.api.dto.user;

import co.com.bancolombia.model.response.PetitionItemDto;
import co.com.bancolombia.model.response.UserDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataGroupDto {
    private UserDto user;
    private List<PetitionItemDto> loanPetitions;
    private BigDecimal totalMonthlyDebt;
}

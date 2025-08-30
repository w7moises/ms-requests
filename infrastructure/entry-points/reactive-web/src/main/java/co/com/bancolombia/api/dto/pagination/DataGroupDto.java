package co.com.bancolombia.api.dto.pagination;


import co.com.bancolombia.api.dto.loanpetition.PetitionItemDto;
import co.com.bancolombia.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

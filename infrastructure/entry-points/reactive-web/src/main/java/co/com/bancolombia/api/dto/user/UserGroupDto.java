package co.com.bancolombia.api.dto.user;

import co.com.bancolombia.api.dto.loanpetition.PetitionItemDto;
import co.com.bancolombia.api.dto.pagination.AggregatesDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupDto {
    private UserDto user;
    private AggregatesDto aggregates;
    private List<PetitionItemDto> items;
}

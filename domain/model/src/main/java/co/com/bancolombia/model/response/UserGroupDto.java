package co.com.bancolombia.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

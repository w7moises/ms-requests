package co.com.bancolombia.api.dto.pagination;

import co.com.bancolombia.api.dto.user.UserGroupDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedGroupedResponse {
    private PageDto page;
    private List<UserGroupDto> groups;
}

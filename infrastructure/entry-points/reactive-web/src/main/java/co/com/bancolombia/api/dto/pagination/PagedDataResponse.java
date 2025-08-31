package co.com.bancolombia.api.dto.pagination;

import co.com.bancolombia.api.dto.user.DataGroupDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedDataResponse {
    private PageDto page;
    private List<DataGroupDto> data;
}

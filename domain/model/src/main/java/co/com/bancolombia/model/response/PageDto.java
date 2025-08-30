package co.com.bancolombia.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
}

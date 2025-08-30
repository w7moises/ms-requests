package co.com.bancolombia.api.dto.pagination;

import lombok.*;

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

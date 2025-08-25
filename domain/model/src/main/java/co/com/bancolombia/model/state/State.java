package co.com.bancolombia.model.state;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class State {
    private Long id;
    private String name;
    private String description;
}

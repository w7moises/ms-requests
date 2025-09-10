package co.com.bancolombia.model.loanpetition;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserInfoToLambda {
    private String email;
    private String state;
}

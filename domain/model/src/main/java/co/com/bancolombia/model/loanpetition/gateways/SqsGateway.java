package co.com.bancolombia.model.loanpetition.gateways;

import co.com.bancolombia.model.loanpetition.UserInfoToLambda;
import co.com.bancolombia.model.response.LoanPetitionInformation;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface SqsGateway {
    Mono<Boolean> sendMessageToUser(UserInfoToLambda userInfoToLambda);

    Mono<Boolean> sendInfoToLambdaDebtCapacity(List<LoanPetitionInformation> loanPetitions, BigDecimal salary);
}

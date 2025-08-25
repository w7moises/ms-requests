package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(StateHandler stateHandler, LoanTypeHandler loanTypeHandler,
                                                         LoanPetitionHandler loanPetitionHandler) {
        return RouterFunctions
                .route()
                .path("/api/v1/states", builder -> builder
                        .GET("", stateHandler::getAllStates)
                        .POST("", stateHandler::createState)
                        .GET("/{id}", stateHandler::findStateById)
                        .PUT("/{id}", stateHandler::updateState))
                .path("/api/v1/loanTypes", builder -> builder
                        .GET("", loanTypeHandler::getAllLoanTypes)
                        .POST("", loanTypeHandler::createLoanType)
                        .GET("/{id}", loanTypeHandler::getLoanTypeById)
                        .PUT("/{id}", loanTypeHandler::updateLoanType)
                        .DELETE("/{id}", loanTypeHandler::deleteLoanType))
                .path("/api/v1/loanPetitions", builder -> builder
                        .GET("", loanPetitionHandler::getAllPetitions)
                        .POST("", loanPetitionHandler::createPetition)
                        .GET("/email/{email}", loanPetitionHandler::getPetitionsByEmail)
                        .GET("/document/{documentNumber}", loanPetitionHandler::getPetitionsByDocumentNumber))
                .build();
    }
}

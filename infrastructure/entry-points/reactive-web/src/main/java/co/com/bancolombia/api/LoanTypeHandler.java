package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.loantype.CreateLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.EditLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.LoanTypeDto;
import co.com.bancolombia.api.mapper.LoanTypeDtoMapper;
import co.com.bancolombia.usecase.loantype.LoanTypeUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanTypeHandler {
    private final LoanTypeUseCase loanTypeUseCase;
    private final Validator validator;
    private final LoanTypeDtoMapper loanTypeDtoMapper;

    public Mono<ServerResponse> createLoanType(ServerRequest request) {
        return request.bodyToMono(CreateLoanTypeDto.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty())
                        return Mono.error(new ConstraintViolationException(violations));
                    return loanTypeUseCase.saveLoanType(loanTypeDtoMapper.toModel(dto))
                            .flatMap(data -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(data));
                });
    }

    public Mono<ServerResponse> updateLoanType(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(EditLoanTypeDto.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty())
                        return Mono.error(new ConstraintViolationException(violations));
                    var loanTypeToUpdate = loanTypeDtoMapper.toModel(dto);
                    loanTypeToUpdate.setId(id);
                    return loanTypeUseCase.updateLoanType(loanTypeToUpdate)
                            .flatMap(data -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(data));
                });
    }

    public Mono<ServerResponse> getLoanTypeById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return loanTypeUseCase.findLoanTypeById(id)
                .map(loanTypeDtoMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> getAllLoanTypes() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loanTypeUseCase.findAllLoanTypes(), LoanTypeDto.class);
    }

    public Mono<ServerResponse> deleteLoanType(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return loanTypeUseCase.deleteLoanType(id)
                .then(ServerResponse.noContent().build());
    }
}

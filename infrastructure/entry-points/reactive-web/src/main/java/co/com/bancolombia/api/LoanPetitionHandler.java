package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.loanpetition.CreateLoanPetitionDto;
import co.com.bancolombia.api.dto.loanpetition.LoanPetitionDto;
import co.com.bancolombia.api.mapper.LoanPetitionDtoMapper;
import co.com.bancolombia.usecase.loanpetition.LoanPetitionUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.api.utils.TokenInformationMapper.getDocumentNumberFromToken;

@Component
@RequiredArgsConstructor
public class LoanPetitionHandler {

    private final LoanPetitionUseCase loanPetitionUseCase;
    private final Validator validator;
    private final LoanPetitionDtoMapper loanPetitionDtoMapper;

    @PreAuthorize("hasRole('USER')")
    public Mono<ServerResponse> createPetition(ServerRequest request) {
        return request.bodyToMono(CreateLoanPetitionDto.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty())
                        return Mono.error(new ConstraintViolationException(violations));
                    return getDocumentNumberFromToken().flatMap(documentNumber -> {
                        if (!documentNumber.equals(dto.documentNumber()))
                            return Mono.error(new RuntimeException("Document number mismatch"));
                        else
                            return loanPetitionUseCase.savePetition(loanPetitionDtoMapper.toModel(dto))
                                    .map(loanPetitionDtoMapper::toResponse)
                                    .flatMap(data -> ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(data));
                    });
                });
    }

    public Mono<ServerResponse> getAllPetitions() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loanPetitionUseCase.findAllPetitions(), LoanPetitionDto.class);
    }

    public Mono<ServerResponse> getPetitionsByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loanPetitionUseCase.findAllPetitionsByEmail(email), LoanPetitionDto.class);
    }

    public Mono<ServerResponse> getPetitionsByDocumentNumber(ServerRequest request) {
        String documentNumber = request.pathVariable("documentNumber");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loanPetitionUseCase.findAllPetitionsByDocumentNumber(documentNumber), LoanPetitionDto.class);
    }
}

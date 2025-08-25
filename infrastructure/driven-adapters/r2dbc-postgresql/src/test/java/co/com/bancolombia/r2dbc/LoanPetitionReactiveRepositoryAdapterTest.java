package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.r2dbc.entity.LoanPetitionEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.repository.LoanPetitionReactiveRepository;
import co.com.bancolombia.r2dbc.repository.LoanPetitionReactiveRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanPetitionReactiveRepositoryAdapterTest {
    @InjectMocks
    private LoanPetitionReactiveRepositoryAdapter adapter;

    @Mock
    private LoanPetitionReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private LoanPetitionEntity petitionEntity;
    private LoanPetition petition;

    @BeforeEach
    void setUp() {
        petitionEntity = LoanPetitionEntity.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(2500))
                .term(12)
                .email("w@mail.com")
                .documentNumber("12345678")
                .stateId(1L)
                .loanTypeId(1L)
                .build();
        petition = LoanPetition.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(2500))
                .term(12)
                .email("w@mail.com")
                .documentNumber("12345678")
                .stateId(1L)
                .loanTypeId(1L)
                .build();
    }

    @Test
    void shouldSavePetition() {
        when(mapper.map(petition, LoanPetitionEntity.class)).thenReturn(petitionEntity);
        when(repository.save(petitionEntity)).thenReturn(Mono.just(petitionEntity));
        when(mapper.map(petitionEntity, LoanPetition.class)).thenReturn(petition);
        StepVerifier.create(adapter.savePetition(petition))
                .expectNext(petition)
                .verifyComplete();
    }

    @Test
    void shouldFindAllPetitions() {
        when(repository.findAll()).thenReturn(Flux.just(petitionEntity));
        when(mapper.map(petitionEntity, LoanPetition.class)).thenReturn(petition);
        StepVerifier.create(adapter.findAllPetitions())
                .expectNext(petition)
                .verifyComplete();
    }

    @Test
    void shouldFindPetitionsByDocumentNumber() {
        when(repository.findAllByDocumentNumber("12345678")).thenReturn(Flux.just(petitionEntity));
        when(mapper.map(petitionEntity, LoanPetition.class)).thenReturn(petition);
        Flux<LoanPetition> data = adapter.findAllPetitionsByDocumentNumber("12345678");
        StepVerifier.create(data)
                .expectNext(petition)
                .verifyComplete();
    }

    @Test
    void shouldFindPetitionsByEmail() {
        String email = "w@gmail.com";
        when(repository.findAllByEmail(email)).thenReturn(Flux.just(petitionEntity));
        when(mapper.map(petitionEntity, LoanPetition.class)).thenReturn(petition);
        StepVerifier.create(adapter.findAllPetitionsByEmail(email))
                .expectNext(petition)
                .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundWhenPetitionsByEmailNotExist() {
        String email = "no@gmail.com";
        when(repository.findAllByEmail(email)).thenReturn(Flux.empty());
        StepVerifier.create(adapter.findAllPetitionsByEmail(email))
                .expectError(NotFoundException.class)
                .verify();
    }
}

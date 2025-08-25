package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.loanpetition.CreateLoanPetitionDto;
import co.com.bancolombia.api.dto.loanpetition.LoanPetitionDto;
import co.com.bancolombia.api.dto.loantype.CreateLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.EditLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.LoanTypeDto;
import co.com.bancolombia.model.state.State;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/states",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = StateHandler.class,
                    beanMethod = "getAllStates",
                    operation = @Operation(
                            operationId = "getAllStates",
                            summary = "List all states",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of states",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = State.class))
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/states/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = StateHandler.class,
                    beanMethod = "findStateById",
                    operation = @Operation(
                            operationId = "findStateById",
                            summary = "Get state by ID",
                            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "State found",
                                            content = @Content(schema = @Schema(implementation = State.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/states",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = StateHandler.class,
                    beanMethod = "createState",
                    operation = @Operation(
                            operationId = "createState",
                            summary = "Create a new state",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = State.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Created",
                                            content = @Content(schema = @Schema(implementation = State.class))),
                                    @ApiResponse(responseCode = "400", description = "Validation failed")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/states/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = StateHandler.class,
                    beanMethod = "updateState",
                    operation = @Operation(
                            operationId = "updateState",
                            summary = "Update a state by ID",
                            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = State.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Updated",
                                            content = @Content(schema = @Schema(implementation = State.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanTypes",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = LoanTypeHandler.class,
                    beanMethod = "getAllLoanTypes",
                    operation = @Operation(
                            operationId = "getAllLoanTypes",
                            summary = "List all loan types",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of loan types",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = LoanTypeDto.class))
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanTypes/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = LoanTypeHandler.class,
                    beanMethod = "getLoanTypeById",
                    operation = @Operation(
                            operationId = "getLoanTypeById",
                            summary = "Get loan type by ID",
                            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Loan type found",
                                            content = @Content(schema = @Schema(implementation = LoanTypeDto.class))),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanTypes",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = LoanTypeHandler.class,
                    beanMethod = "createLoanType",
                    operation = @Operation(
                            operationId = "createLoanType",
                            summary = "Create loan type",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateLoanTypeDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Created",
                                            content = @Content(schema = @Schema(implementation = LoanTypeDto.class))),
                                    @ApiResponse(responseCode = "400", description = "Validation failed")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanTypes/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = LoanTypeHandler.class,
                    beanMethod = "updateLoanType",
                    operation = @Operation(
                            operationId = "updateLoanType",
                            summary = "Update loan type",
                            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = EditLoanTypeDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Updated"),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanTypes/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE,
                    beanClass = LoanTypeHandler.class,
                    beanMethod = "deleteLoanType",
                    operation = @Operation(
                            operationId = "deleteLoanType",
                            summary = "Delete loan type",
                            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Deleted"),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanPetitions",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = LoanPetitionHandler.class,
                    beanMethod = "getAllPetitions",
                    operation = @Operation(
                            operationId = "getAllPetitions",
                            summary = "List all loan petitions",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of loan petitions",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = LoanPetitionDto.class))
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanPetitions/email/{email}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = LoanPetitionHandler.class,
                    beanMethod = "getPetitionsByEmail",
                    operation = @Operation(
                            operationId = "getPetitionsByEmail",
                            summary = "Get petitions by email",
                            parameters = {@Parameter(name = "email", in = ParameterIn.PATH, required = true)},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Loan petitions found",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = LoanPetitionDto.class))
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanPetitions/document/{documentNumber}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = LoanPetitionHandler.class,
                    beanMethod = "getPetitionsByDocumentNumber",
                    operation = @Operation(
                            operationId = "getPetitionsByDocumentNumber",
                            summary = "Get petitions by document number",
                            parameters = {@Parameter(name = "documentNumber", in = ParameterIn.PATH, required = true)},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Loan petitions found",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = LoanPetitionDto.class))
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/loanPetitions",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = LoanPetitionHandler.class,
                    beanMethod = "createPetition",
                    operation = @Operation(
                            operationId = "createPetition",
                            summary = "Create a loan petition",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateLoanPetitionDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Created",
                                            content = @Content(schema = @Schema(implementation = LoanPetitionDto.class))),
                                    @ApiResponse(responseCode = "400", description = "Validation failed")
                            }
                    )
            )
    })
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

package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.loanpetition.CreateLoanPetitionDto;
import co.com.bancolombia.api.dto.loanpetition.EditLoanPetitionDto;
import co.com.bancolombia.api.dto.loanpetition.LoanPetitionDto;
import co.com.bancolombia.model.loanpetition.LoanPetition;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanPetitionDtoMapper {

    LoanPetitionDto toResponse(LoanPetition loanPetition);

    List<LoanPetitionDto> toResponseList(List<LoanPetition> loanPetitions);

    LoanPetition toModel(CreateLoanPetitionDto createLoanPetitionDto);

    LoanPetition toModel(EditLoanPetitionDto editLoanPetitionDto);

}

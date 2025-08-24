package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.loantype.CreateLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.EditLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.LoanTypeDto;
import co.com.bancolombia.model.loanpetition.LoanPetition;
import co.com.bancolombia.model.loantype.LoanType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanTypeDtoMapper {
    LoanTypeDto toResponse(LoanType loanType);

    List<LoanTypeDto> toResponseList(List<LoanType> loanTypes);

    LoanPetition toModel(CreateLoanTypeDto createLoanTypeDto);

    LoanPetition toModel(EditLoanTypeDto editLoanTypeDto);
}

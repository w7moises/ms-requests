package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.loantype.CreateLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.EditLoanTypeDto;
import co.com.bancolombia.api.dto.loantype.LoanTypeDto;
import co.com.bancolombia.model.loantype.LoanType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanTypeDtoMapper {
    LoanTypeDto toResponse(LoanType loanType);

    List<LoanTypeDto> toResponseList(List<LoanType> loanTypes);

    LoanType toModel(CreateLoanTypeDto createLoanTypeDto);

    LoanType toModel(EditLoanTypeDto editLoanTypeDto);
}

package com.excercises.swiftAPI.components.mappers;

import com.excercises.swiftAPI.models.BankEntity;
import com.excercises.swiftAPI.models.DTOs.BankDTO;
import com.excercises.swiftAPI.models.DTOs.CountryDTO;
import com.excercises.swiftAPI.models.DTOs.ReducedBankDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperToDTO {
    public ReducedBankDTO toReducedBankDTO(BankEntity bankEntity) {
        return ReducedBankDTO.builder()
                .swiftCode(bankEntity.getSwiftCode())
                .address(bankEntity.getAddress())
                .bankName(bankEntity.getBankName())
                .countryISO2(bankEntity.getCountryISO2())
                .isHeadquarter(bankEntity.isHeadquarter())
                .build();
    }

    public BankDTO toBankDTO(BankEntity bankEntity) {
        List<ReducedBankDTO> reducedBranches = bankEntity.getBranches().stream()
                .map(this::toReducedBankDTO)
                .toList();

        return BankDTO.builder()
                .address(bankEntity.getAddress())
                .bankName(bankEntity.getBankName())
                .countryISO2(bankEntity.getCountryISO2())
                .countryName(bankEntity.getCountryName())
                .isHeadquarter(bankEntity.isHeadquarter())
                .swiftCode(bankEntity.getSwiftCode())
                .branches(bankEntity.isHeadquarter() ? reducedBranches : null)
                .build();
    }

    public CountryDTO toCountryDTO(String countryISO2, String countryName, List<BankEntity> bankEntities) {
        List<ReducedBankDTO> swiftCodes = bankEntities.stream()
                .filter(bank -> bank.getCountryISO2().equals(countryISO2))
                .map(this::toReducedBankDTO)
                .collect(Collectors.toList());

        return CountryDTO.builder()
                .countryISO2(countryISO2)
                .countryName(countryName)
                .swiftCodes(swiftCodes)
                .build();
    }
}
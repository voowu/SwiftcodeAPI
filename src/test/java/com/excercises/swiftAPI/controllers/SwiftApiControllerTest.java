package com.excercises.swiftAPI.controllers;

import com.excercises.swiftAPI.models.BankEntity;
import com.excercises.swiftAPI.models.DTOs.CountryDTO;
import com.excercises.swiftAPI.models.DTOs.ReducedBankDTO;
import com.excercises.swiftAPI.repositories.SwiftApiRepository;
import com.excercises.swiftAPI.services.SwiftApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SwiftApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftApiRepository swiftApiRepository;

    @Mock
    private SwiftApiService swiftApiService;

    @InjectMocks
    private SwiftApiController swiftApiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        swiftApiRepository.deleteAll();

        BankEntity bankEntity = BankEntity.builder()
                .swiftCode("15935745XXX")
                .address("Berlin")
                .bankName("Bank A")
                .countryISO2("GE")
                .countryName("GERMANY")
                .isHeadquarter(true)
                .build();
        swiftApiRepository.save(bankEntity);

        BankEntity anotherBank = BankEntity.builder()
                .swiftCode("87654321XXX")
                .address("Sydney")
                .bankName("Bank F")
                .countryISO2("AU")
                .countryName("AUSTRALIA")
                .isHeadquarter(true)
                .build();
        swiftApiRepository.save(anotherBank);

        BankEntity anotherBank1 = BankEntity.builder()
                .swiftCode("87654321AAA")
                .address("NearSydney")
                .bankName("Bank G")
                .countryISO2("AU")
                .countryName("AUSTRALIA")
                .isHeadquarter(false)
                .build();
        swiftApiRepository.save(anotherBank1);
    }

    @Test
    public void SwiftApiControllerTest_GetDetails() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/{swift-code}", "15935745XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode", is("15935745XXX")))
                .andExpect(jsonPath("$.bankName", is("Bank A")));
    }

    @Test
    public void SwiftApiControllerTest_DeleteSwiftCode() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/{swift-code}", "87654321XXX")
                        .param("bankName", "Bank F")
                        .param("countryISO2", "AU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Deleted headquarter 87654321XXX and " +
                        "associated branches: AAA, ")));
    }

    @Test
    public void SwiftApiControllerTest_GetBankEntitiesByCountryISO2Code() throws Exception {
        String countryISO2 = "GE";

        ReducedBankDTO reducedBankDTO = ReducedBankDTO.builder()
                .swiftCode("15935745XXX")
                .address("Berlin")
                .bankName("Bank A")
                .countryISO2("GE")
                .isHeadquarter(true)
                .build();

        CountryDTO countryDTO = new CountryDTO(countryISO2, "GERMANY", List.of(reducedBankDTO));

        when(swiftApiService.findAllSwiftCodesWithDetailsByCountryISO2(countryISO2))
                .thenReturn(countryDTO);

        mockMvc.perform(get("/v1/swift-codes/country/{countyISO2code}", countryISO2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2", is("GE")))
                .andExpect(jsonPath("$.countryName", is("GERMANY")))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode", is("15935745XXX")));
    }
}

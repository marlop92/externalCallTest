package pl.mlopatka.externalcalltest.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.mlopatka.externalcalltest.currency.CurrencyStatsDto;
import pl.mlopatka.externalcalltest.utils.StubUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class CurrencyControllerIT {

    private static final String AVG_CURRENCY_VALUE = "/currencies/avgRates/{currencyCode}";
    private static final String CURRENCY_CODE = "USD";
    private static final String START_DATE = "2020-01-01";
    private static final String END_DATE = "2020-01-30";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @RegisterExtension
    static WireMockExtension WIRE_MOCK_INSTANCE = WireMockExtension.newInstance()
            .options(wireMockConfig()
                    .dynamicPort())
            .build();

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("rates-api.host", () -> "http://localhost:" + WIRE_MOCK_INSTANCE.getPort());
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        StubUtils.stubCurrencyRatesApi(WIRE_MOCK_INSTANCE, CURRENCY_CODE, START_DATE, END_DATE);
    }

    @Test
    void shouldGetAvgCurrencyResponse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(AVG_CURRENCY_VALUE, CURRENCY_CODE)
                        .queryParam("startDate", START_DATE)
                        .queryParam("endDate", END_DATE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String rawResponse = mvcResult.getResponse().getContentAsString();
        CurrencyStatsDto currencyStatsDto = objectMapper.readValue(rawResponse, CurrencyStatsDto.class);

        CurrencyStatsDto expectedStatsDto = CurrencyStatsDto.builder()
                .currencyCode(CURRENCY_CODE)
                .startDate(LocalDate.parse(START_DATE))
                .endDate(LocalDate.parse(END_DATE))
                .avgRate(new BigDecimal("3.2"))
                .build();

        assertEquals(expectedStatsDto, currencyStatsDto);
    }
}

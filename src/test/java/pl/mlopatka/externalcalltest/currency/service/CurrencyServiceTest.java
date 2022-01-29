package pl.mlopatka.externalcalltest.currency.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.mlopatka.externalcalltest.config.CurrencyApiConfig;
import pl.mlopatka.externalcalltest.currency.Currency;
import pl.mlopatka.externalcalltest.currency.CurrencyStatsDto;
import pl.mlopatka.externalcalltest.currency.Rate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;
    @Mock
    private CurrencyApiConfig mockConfiguration;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void shouldCalculateAvgCurrencyValue() {
        String mockUrl = "mockUrl";
        when(mockConfiguration.getUrl()).thenReturn(mockUrl);
        when(mockRestTemplate.getForEntity(eq(mockUrl), eq(Currency.class), any(Map.class)))
                .thenReturn(createCurrencyResponse());

        CurrencyStatsDto resultCurrencyStats = currencyService.getAvgCurrencyValue("PLN",
                LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-30"));

        CurrencyStatsDto expectedCurrencyStats = CurrencyStatsDto.builder()
                .currencyCode("PLN")
                .startDate(LocalDate.parse("2020-01-01"))
                .endDate(LocalDate.parse("2020-01-30"))
                .avgRate(new BigDecimal("1.5")).build();

        assertEquals(expectedCurrencyStats, resultCurrencyStats);
    }

    private ResponseEntity<Currency> createCurrencyResponse() {
        List<Rate> rates = List.of(
                Rate.builder().mid(new BigDecimal("1.0")).build(),
                Rate.builder().mid(new BigDecimal("1.5")).build(),
                Rate.builder().mid(new BigDecimal("2.0")).build()
        );

        Currency currency = Currency.builder().rates(rates).build();
        return ResponseEntity.ok(currency);
    }
}
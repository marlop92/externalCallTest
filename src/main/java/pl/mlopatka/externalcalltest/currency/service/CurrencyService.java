package pl.mlopatka.externalcalltest.currency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mlopatka.externalcalltest.config.CurrencyApiConfig;
import pl.mlopatka.externalcalltest.currency.Currency;
import pl.mlopatka.externalcalltest.currency.CurrencyStatsDto;
import pl.mlopatka.externalcalltest.currency.CurrencyTable;
import pl.mlopatka.externalcalltest.currency.Rate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;
import static pl.mlopatka.externalcalltest.currency.Constants.*;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final CurrencyApiConfig currencyApiConfig;

    public CurrencyStatsDto getAvgCurrencyValue(String currencyCode, LocalDate startDate, LocalDate endDate) {
        Currency currencyRates = getCurrencyRates(currencyCode, startDate, endDate);
        List<Rate> rates = isEmpty(currencyRates.getRates()) ? Collections.emptyList() : currencyRates.getRates();
        BigDecimal avgRate = calculateAvgRate(rates);
        return new CurrencyStatsDto(currencyCode, startDate, endDate, avgRate);
    }

    private BigDecimal calculateAvgRate(List<Rate> rates) {
        BigDecimal ratesSum =  rates.stream()
                .map(Rate::getMid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return isEmpty(rates) ? BigDecimal.ZERO : ratesSum.divide(new BigDecimal(rates.size()), RoundingMode.FLOOR);
    }

    private Currency getCurrencyRates(String currencyCode, LocalDate startDate, LocalDate endDate) {
        Map<String, ?> ratesPathVariables = Map.of(
                RATES_TABLE_PARAM, CurrencyTable.A,
                RATES_CURRENCY_CODE_PARAM, currencyCode,
                RATES_START_DATE_PARAM, startDate,
                RATES_END_DATE_PARAM, endDate);

        ResponseEntity<Currency> rates = restTemplate.getForEntity(currencyApiConfig.getUrl(), Currency.class,
                ratesPathVariables);

        return rates.getBody();
    }
}

package pl.mlopatka.externalcalltest.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CurrencyStatsDto {

    private String currencyCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal avgRate;
}

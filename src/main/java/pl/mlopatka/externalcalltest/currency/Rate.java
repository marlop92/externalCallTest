package pl.mlopatka.externalcalltest.currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rate {

    private String no;
    private LocalDate effectiveDate;
    private BigDecimal mid;

}

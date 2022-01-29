package pl.mlopatka.externalcalltest.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private String table;
    @JsonProperty("currency")
    private String currencyName;
    @JsonProperty("code")
    private String currencyCode;
    private List<Rate> rates;
}

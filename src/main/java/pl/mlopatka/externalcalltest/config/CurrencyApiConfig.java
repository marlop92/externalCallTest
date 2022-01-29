package pl.mlopatka.externalcalltest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Validated
@ConfigurationProperties("rates-api")
public class CurrencyApiConfig {

    @NotEmpty
    private String url;
}

package pl.mlopatka.externalcalltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import pl.mlopatka.externalcalltest.config.CurrencyApiConfig;

@SpringBootApplication
@Import({CurrencyApiConfig.class})
public class ExternalCallTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalCallTestApplication.class, args);
    }

}

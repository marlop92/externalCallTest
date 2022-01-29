package pl.mlopatka.externalcalltest.utils;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

public class StubUtils {

    public static void stubCurrencyRatesApi(WireMockExtension wm1, String currencyCode, String startDate,
                                            String endDate) {
        wm1.stubFor(get(String.format("/api/exchangerates/rates/A/%s/%s/%s", currencyCode, startDate, endDate))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("json/currency-example.json")));
    }
}

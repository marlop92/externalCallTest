package pl.mlopatka.externalcalltest.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.mlopatka.externalcalltest.currency.CurrencyStatsDto;
import pl.mlopatka.externalcalltest.currency.service.CurrencyService;

import java.time.LocalDate;

@RestController
@RequestMapping("currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("avgRates/{currencyCode}")
    public CurrencyStatsDto getCurrencies(
            @PathVariable("currencyCode") String currencyCode,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return currencyService.getAvgCurrencyValue(currencyCode, startDate, endDate);
    }
}

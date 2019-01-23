package uk.co.datadisk.microservices.currencyconversionservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import uk.co.datadisk.microservices.currencyconversionservice.entities.CurrencyConversionBean;
import uk.co.datadisk.microservices.currencyconversionservice.proxies.CurrencyExchangeServiceProxy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    @GetMapping("/currency-converter/from/{fromCurrency}/to/{toCurrency}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String fromCurrency,
                                                  @PathVariable String toCurrency,
                                                  @PathVariable BigDecimal quantity) {

        // create mapping for ResponseEntity variables
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("fromCurrency", fromCurrency);
        uriVariables.put("toCurrency", toCurrency);

        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{fromCurrency}/to/{toCurrency}",
                CurrencyConversionBean.class,
                uriVariables);

        CurrencyConversionBean response = responseEntity.getBody();

        // Using Feign to solve the complex code ssee method below

        System.out.println(response.getTotalCalculatedAmount());
        System.out.println(response.getId());
        System.out.println(response.getFromCurrency());

        return new CurrencyConversionBean(
                response.getId(),
                fromCurrency,
                toCurrency,
                response.getConversionMultiple(),
                quantity,
                quantity.multiply(response.getConversionMultiple()),
                response.getPort());
    }

    // Using Feign to solve the complex code above
    @GetMapping("/currency-converter-feign/from/{fromCurrency}/to/{toCurrency}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(@PathVariable String fromCurrency,
                                                  @PathVariable String toCurrency,
                                                  @PathVariable BigDecimal quantity) {

        // Use the proxy to get data from the currency exchange service
        CurrencyConversionBean response = proxy.retrieveExchangeValue(fromCurrency, toCurrency);

        return new CurrencyConversionBean(
                response.getId(),
                fromCurrency,
                toCurrency,
                response.getConversionMultiple(),
                quantity,
                quantity.multiply(response.getConversionMultiple()),
                response.getPort());
    }
}

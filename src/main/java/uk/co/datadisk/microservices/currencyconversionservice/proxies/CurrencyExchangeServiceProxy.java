package uk.co.datadisk.microservices.currencyconversionservice.proxies;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.datadisk.microservices.currencyconversionservice.entities.CurrencyConversionBean;

//@FeignClient(name = "currency-exchange-service", url = "localhost:8000")
//@FeignClient(name = "currency-exchange-service")
@FeignClient(name = "netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

    //@GetMapping("/currency-exchange-service/currency-exchange/from/{fromCurrency}/to/{toCurrency}")
    @GetMapping("/currency-exchange-service/currency-exchange/from/{fromCurrency}/to/{toCurrency}")
    CurrencyConversionBean retrieveExchangeValue(@PathVariable String fromCurrency,
                                                        @PathVariable String toCurrency);

    //old way
//    public CurrencyConversionBean retrieveExchangeValue(@PathVariable("fromCurrency") String fromCurrency,
//                                                        @PathVariable("toCurrency") String toCurrency);

}

package com.exchanger.service;

import com.exchanger.dao.ExchangeRatesDao;
import com.exchanger.dto.ExchangeRateDto;
import com.exchanger.exception.ExchangeRateNotFoundException;
import com.exchanger.exception.InvalidCurrenciesPathException;
import com.exchanger.exception.MissingRequestParameterException;
import com.exchanger.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public class ExchangeRatesService {
    private final ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao();

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRatesDao.findAll();
    }

    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        if (baseCurrencyCode == null || baseCurrencyCode.isBlank() ||
                targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidCurrenciesPathException();
        }
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate setExchangeRate(ExchangeRateDto exchangeRateDto) {
        if (exchangeRateDto.baseCurrencyCode() == null || exchangeRateDto.baseCurrencyCode().isBlank() ||
                exchangeRateDto.targetCurrencyCode() == null || exchangeRateDto.targetCurrencyCode().isBlank() ||
                exchangeRateDto.rate() == null) {
            throw new MissingRequestParameterException();
        }
        exchangeRatesDao.save(exchangeRateDto);
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(
                exchangeRateDto.baseCurrencyCode(),
                exchangeRateDto.targetCurrencyCode()
        );
        return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate uploadExchangeRate(ExchangeRateDto exchangeRateDto) {
        if (exchangeRateDto.baseCurrencyCode() == null || exchangeRateDto.baseCurrencyCode().isBlank() ||
                exchangeRateDto.targetCurrencyCode() == null || exchangeRateDto.targetCurrencyCode().isBlank() ||
                exchangeRateDto.rate() == null) {
            throw new MissingRequestParameterException();
        }
        exchangeRatesDao.updateExchangeRate(exchangeRateDto);
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(
                exchangeRateDto.baseCurrencyCode(),
                exchangeRateDto.targetCurrencyCode()
        );
        return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);

    }
}

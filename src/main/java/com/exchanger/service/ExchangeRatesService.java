package com.exchanger.service;

import com.exchanger.dao.ExchangeRatesDao;
import com.exchanger.dto.ExchangeRequestDto;
import com.exchanger.dto.ExchangeResponseDto;
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
        validate(baseCurrencyCode, targetCurrencyCode, new InvalidCurrenciesPathException());
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(baseCurrencyCode, targetCurrencyCode);
        return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate setExchangeRate(ExchangeRateDto exchangeRateDto) {
        validate(exchangeRateDto.baseCurrencyCode(), exchangeRateDto.targetCurrencyCode(), new MissingRequestParameterException());
        exchangeRatesDao.save(exchangeRateDto);
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(
                exchangeRateDto.baseCurrencyCode(),
                exchangeRateDto.targetCurrencyCode()
        );
        return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate uploadExchangeRate(ExchangeRateDto exchangeRateDto) {
        validate(exchangeRateDto.baseCurrencyCode(), exchangeRateDto.targetCurrencyCode(), new MissingRequestParameterException());
        exchangeRatesDao.updateExchangeRate(exchangeRateDto);
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(
                exchangeRateDto.baseCurrencyCode(),
                exchangeRateDto.targetCurrencyCode()
        );
        return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);

    }

    public ExchangeResponseDto exchange(ExchangeRequestDto exchangeRequestDto) {
        validate(exchangeRequestDto.fromCurrencyCode(), exchangeRequestDto.toCurrencyCode(), new MissingRequestParameterException());
        double rate = exchangeRatesDao.findRate(
                exchangeRequestDto.fromCurrencyCode(),
                exchangeRequestDto.toCurrencyCode()
        );
        double convertedAmount = rate * exchangeRequestDto.amount();
        Optional<ExchangeRate> exchangeRate = exchangeRatesDao.findByCodes(
                exchangeRequestDto.fromCurrencyCode(),
                exchangeRequestDto.toCurrencyCode()
        );
        return new ExchangeResponseDto(
                exchangeRate.orElseThrow(ExchangeRateNotFoundException::new),
                rate,
                exchangeRequestDto.amount(),
                convertedAmount
        );
    }

    private void validate(String baseCurrencyCode, String targetCurrencyCode, RuntimeException exception) {
        if (baseCurrencyCode == null || baseCurrencyCode.isBlank() ||
                targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw exception;
        }
    }
}
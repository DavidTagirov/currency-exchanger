package com.exchanger.service;

import com.exchanger.dao.CurrenciesDao;
import com.exchanger.dto.CurrencyDto;
import com.exchanger.exception.CurrencyNotFoundException;
import com.exchanger.exception.InvalidCurrencyPathException;
import com.exchanger.exception.MissingRequestParameterException;
import com.exchanger.model.Currency;

import java.util.List;
import java.util.Optional;

public class CurrenciesService {
    private final CurrenciesDao currenciesDao = new CurrenciesDao();

    public List<Currency> getAllCurrencies() {
        return currenciesDao.findAll();
    }

    public Currency getCurrency(String code) {
        if (code == null || code.length() != 3) {
            throw new InvalidCurrencyPathException();
        }
        Optional<Currency> currency = currenciesDao.findByCode(code);
        return currency.orElseThrow(CurrencyNotFoundException::new);
    }

    public void setCurrency(CurrencyDto currencyDto) {
        if (currencyDto.code() == null || currencyDto.code().length() != 3 ||
                currencyDto.fullName() == null || currencyDto.fullName().isBlank() ||
                currencyDto.sign() == null || currencyDto.sign().isBlank()) {
            throw new MissingRequestParameterException();
        }
        currenciesDao.save(currencyDto);
    }
}

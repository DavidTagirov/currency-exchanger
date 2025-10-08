package com.exchanger.service;

import com.exchanger.dao.CurrenciesDao;
import com.exchanger.model.Currency;
import com.google.gson.Gson;

import java.util.List;
import java.util.Optional;

public class CurrenciesService {
    private final CurrenciesDao currenciesDao = new CurrenciesDao();

    public String getAllCurrencies() {
        List<Currency> currencies = currenciesDao.findAll();
        return new Gson().toJson(currencies);
    }

    public String getCurrency(String code) {
        Optional<Currency> currency = currenciesDao.findByCode(code);
        return new Gson().toJson(currency);
    }
}

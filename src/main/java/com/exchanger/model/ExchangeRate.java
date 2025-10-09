package com.exchanger.model;

public record ExchangeRate(long id, Currency baseCurrency, Currency targetCurrency, double rate) {
}

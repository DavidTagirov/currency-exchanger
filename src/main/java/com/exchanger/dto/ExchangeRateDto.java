package com.exchanger.dto;

public record ExchangeRateDto(String baseCurrencyCode, String targetCurrencyCode, double rate) {
}

package com.exchanger.dto;

public record ExchangeRateDto(String baseCurrencyCode, String targetCurrencyCode, Double rate) {
}

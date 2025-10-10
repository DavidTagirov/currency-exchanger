package com.exchanger.dto;

public record ExchangeRequestDto(String fromCurrencyCode, String toCurrencyCode, double amount) {
}

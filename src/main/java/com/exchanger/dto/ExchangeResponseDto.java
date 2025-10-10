package com.exchanger.dto;

import com.exchanger.model.ExchangeRate;

public record ExchangeResponseDto(ExchangeRate exchangeRate, double rate, double amount, double convertedAmount) {
}
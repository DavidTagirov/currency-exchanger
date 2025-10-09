package com.exchanger.mapper;

import com.exchanger.model.Currency;
import com.exchanger.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRatesMapper {
    public static ExchangeRate map(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("exchangeRateId"),
                new Currency(
                        resultSet.getLong("baseCurrencyId"),
                        resultSet.getString("baseCode"),
                        resultSet.getString("baseFullName"),
                        resultSet.getString("baseSign")
                ),
                new Currency(
                        resultSet.getLong("targetCurrencyId"),
                        resultSet.getString("targetCode"),
                        resultSet.getString("targetFullName"),
                        resultSet.getString("targetSign")
                ),
                resultSet.getDouble("rate")
        );
    }
}

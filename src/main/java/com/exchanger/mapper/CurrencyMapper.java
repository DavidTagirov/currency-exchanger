package com.exchanger.mapper;

import com.exchanger.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyMapper {
    public static Currency map(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("fullName"),
                resultSet.getString("sign")
        );
    }
}

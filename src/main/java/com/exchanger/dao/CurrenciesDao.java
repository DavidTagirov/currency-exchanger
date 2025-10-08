package com.exchanger.dao;

import com.exchanger.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao {
    public Optional<Currency> findByCode(String code) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("""
                     SELECT * FROM Currencies WHERE code = ?;
                     """)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.of(new Currency(
                    resultSet.getLong("id"),
                    resultSet.getString("code"),
                    resultSet.getString("fullName"),
                    resultSet.getString("sign")
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                      SELECT * FROM currencies;
                    """);

            while (resultSet.next()) {
                currencies.add(new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullName"),
                        resultSet.getString("sign")
                ));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

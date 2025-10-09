package com.exchanger.dao;

import com.exchanger.dto.CurrencyDto;
import com.exchanger.exception.CurrencyAlreadyExistsException;
import com.exchanger.mapper.CurrencyMapper;
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
            return Optional.of(CurrencyMapper.map(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти валюту" + e);
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
                currencies.add(CurrencyMapper.map(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить список всех валют" + e);
        }
    }

    public void save(CurrencyDto currencyDto) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("""
                     INSERT INTO currencies(code, fullName, sign)
                     VALUES (?, ?, ?);
                     """)) {
            preparedStatement.setString(1, currencyDto.code());
            preparedStatement.setString(2, currencyDto.fullName());
            preparedStatement.setString(3, currencyDto.sign());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new CurrencyAlreadyExistsException();
            } else {
                throw new RuntimeException("Не удалось сохранить валюту" + e);
            }
        }
    }
}

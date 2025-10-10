package com.exchanger.dao;

import com.exchanger.dto.ExchangeRateDto;
import com.exchanger.exception.CurrencyNotExistsException;
import com.exchanger.exception.CurrencyPairNotExistsException;
import com.exchanger.exception.ExchangeRateAlreadyExistsException;
import com.exchanger.model.ExchangeRate;
import com.exchanger.mapper.ExchangeRatesMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao {
    private static final String FIND_ALL_SQL = """
            SELECT
            er.id AS exchangeRateId,
            bc.id AS baseCurrencyId,
            bc.code AS baseCode,
            bc.fullName AS baseFullName,
            bc.sign AS baseSign,
            tc.id AS targetCurrencyId,
            tc.code AS targetCode,
            tc.fullName AS targetFullName,
            tc.sign AS targetSign,
            er.rate AS rate
            FROM ExchangeRates er
            JOIN Currencies bc ON er.baseCurrencyId = bc.id
            JOIN Currencies tc ON er.targetCurrencyId = tc.id
            """;
    private static final String FIND_BY_CODES_SQL = FIND_ALL_SQL + """
            WHERE bc.code = ? AND tc.code = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ExchangeRates(baseCurrencyId, targetCurrencyId, rate)
            VALUES (
                (SELECT id FROM Currencies WHERE code = ?),
                (SELECT id FROM Currencies WHERE code = ?),
                ?
            )
            """;
    private static final String UPDATE_SQL = """
            UPDATE ExchangeRates
            SET rate = ?
            WHERE baseCurrencyId = (SELECT id FROM Currencies WHERE code = ?)
            AND targetCurrencyId = (SELECT id FROM Currencies WHERE code = ?)
            """;

    private static final String FIND_RATE_SQL = """
            SELECT rate FROM ExchangeRates
            WHERE baseCurrencyId = (SELECT id FROM Currencies WHERE code = ?)
            AND targetCurrencyId = (SELECT id FROM Currencies WHERE code = ?)
            """;

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            while (resultSet.next()) {
                exchangeRates.add(ExchangeRatesMapper.map(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить список всех обменных курсов" + e);
        }
    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODES_SQL)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(ExchangeRatesMapper.map(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти обменный курс" + e);
        }
    }

    public void save(ExchangeRateDto exchangeRateDto) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, exchangeRateDto.baseCurrencyCode());
            preparedStatement.setString(2, exchangeRateDto.targetCurrencyCode());
            preparedStatement.setDouble(3, exchangeRateDto.rate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExchangeRateAlreadyExistsException();
            } else if (e.getSQLState().equals("23502") || e.getSQLState().equals("23503")) {
                throw new CurrencyNotExistsException();
            } else {
                throw new RuntimeException("Не удалось сохранить обменный курс" + e);
            }
        }
    }

    public void updateExchangeRate(ExchangeRateDto exchangeRateDto) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setDouble(1, exchangeRateDto.rate());
            preparedStatement.setString(2, exchangeRateDto.baseCurrencyCode());
            preparedStatement.setString(3, exchangeRateDto.targetCurrencyCode());
            int update = preparedStatement.executeUpdate();
            if (update == 0) {
                throw new CurrencyPairNotExistsException();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить обменный курс" + e);
        }
    }

    public double findRate(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_RATE_SQL)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new CurrencyPairNotExistsException();
            }
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить ставку обменного курса" + e);
        }
    }
}

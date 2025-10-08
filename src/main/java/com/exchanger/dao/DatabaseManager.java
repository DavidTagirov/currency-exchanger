package com.exchanger.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static final String url = "jdbc:sqlite:src/main/resources/database/currency.db";
    private static final String schemaSql = "src/main/resources/database/schema.sql";
    private static final String dataSql = "src/main/resources/database/data.sql";

    private DatabaseManager() {
    }

    static {
        runSqlScript(schemaSql);
        runSqlScript(dataSql);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private static void runSqlScript(String resourcePath) {
        InputStream inputStream = DatabaseManager.class.getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("Не найден файл: " + resourcePath);
        }

        String sql = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        try (Statement statement = getConnection().createStatement()) {
            for (String command : sql.split(";")) {
                if (!command.isBlank()) {
                    statement.executeUpdate(command);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось выполнить скрипт" + e);
        }
    }
}

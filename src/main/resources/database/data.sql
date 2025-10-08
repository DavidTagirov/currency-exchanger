INSERT OR IGNORE INTO Currencies(code, fullName, sign)
VALUES ('USD', 'United States Dollar', '$'),
       ('RUB', 'Russian Ruble', '₽'),
       ('EUR', 'Euro', '€'),
       ('GBP', 'Pound Sterling', '£'),
       ('AUD', 'Australian Dollar', 'A$'),
       ('CNY', 'Yuan Renminbi', 'C¥'),
       ('JPY', 'Yen', 'J¥'),
       ('KZT', 'Tenge', '₸');

INSERT OR IGNORE INTO ExchangeRates(baseCurrencyId, targetCurrencyId, rate)
VALUES (1, 2, 81.93),
       (2, 1, 0.012205);
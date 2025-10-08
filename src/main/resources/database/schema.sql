CREATE TABLE IF NOT EXISTS Currencies
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    code     TEXT UNIQUE NOT NULL,
    fullName TEXT        NOT NULL,
    sign     TEXT        NOT NULL
);

CREATE TABLE IF NOT EXISTS ExchangeRates
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    baseCurrencyId   INTEGER NOT NULL,
    targetCurrencyId INTEGER NOT NULL,
    rate             REAL    NOT NULL,
    UNIQUE (baseCurrencyId, targetCurrencyId),
    FOREIGN KEY (baseCurrencyId) REFERENCES Currencies (id),
    FOREIGN KEY (targetCurrencyId) REFERENCES Currencies (id)
);

CREATE INDEX IF NOT EXISTS idx_currencies_code ON Currencies (code);

CREATE INDEX IF NOT EXISTS idx_exchangeRates_currency ON ExchangeRates (baseCurrencyId, targetCurrencyId);
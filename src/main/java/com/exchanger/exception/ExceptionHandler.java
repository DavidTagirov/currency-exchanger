package com.exchanger.exception;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandler {
    private ExceptionHandler() {
    }

    public static void handle(Exception e, HttpServletResponse response) throws IOException {
        int status;
        String message = "";

        if (e instanceof InvalidCurrencyPathException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = "Код валюты отсутствует в адресе";
        } else if (e instanceof CurrencyNotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            message = "Валюта не найдена";
        } else if (e instanceof MissingRequestParameterException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = "Отсутствует нужное поле формы";
        } else if (e instanceof CurrencyAlreadyExistsException) {
            status = HttpServletResponse.SC_CONFLICT;
            message = "Валюта с таким кодом уже существует";
        } else if (e instanceof InvalidCurrenciesPathException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = "Коды валют пары отсутствуют в адресе";
        } else if (e instanceof ExchangeRateNotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            message = "Обменный курс для пары не найден";
        } else if (e instanceof ExchangeRateAlreadyExistsException) {
            status = HttpServletResponse.SC_CONFLICT;
            message = "Валютная пара с таким кодом уже существует";
        } else if (e instanceof CurrencyNotExistsException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            message = "Одна (или обе) валюта из валютной пары не существует в БД";
        } else if (e instanceof CurrencyPairNotExistsException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            message = "Валютная пара отсутствует в базе данных";
        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        response.setStatus(status);
        response.getWriter().write(new Gson().toJson(message));
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("UTF-8");
    }
}

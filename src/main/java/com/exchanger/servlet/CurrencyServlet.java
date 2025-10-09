package com.exchanger.servlet;

import com.exchanger.exception.CurrencyNotFoundException;
import com.exchanger.exception.ExceptionHandler;
import com.exchanger.exception.InvalidCurrencyPathException;
import com.exchanger.model.Currency;
import com.exchanger.service.CurrenciesService;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.InvalidPathException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrenciesService currencyService;

    @Override
    public void init() {
        currencyService = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String code = request.getPathInfo();
            Currency currency = currencyService.getCurrency(code);
            response.getWriter().write(new Gson().toJson(currency));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            ExceptionHandler.handle(e, response);
        }
    }
}

package com.exchanger.servlet;

import com.exchanger.exception.ExceptionHandler;
import com.exchanger.model.ExchangeRate;
import com.exchanger.service.ExchangeRatesService;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;
    @Override
    public void init() {
        exchangeRatesService = new ExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String baseCurrencyCode = request.getPathInfo().substring(0, 3);
            String targetCurrencyCode = request.getPathInfo().substring(3);
            ExchangeRate exchangeRate = exchangeRatesService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            response.getWriter().write(new Gson().toJson(exchangeRate));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            ExceptionHandler.handle(e, response);
        }
    }
}
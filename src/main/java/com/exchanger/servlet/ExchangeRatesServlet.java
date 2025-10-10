package com.exchanger.servlet;

import com.exchanger.dto.ExchangeRateDto;
import com.exchanger.exception.ExceptionHandler;
import com.exchanger.model.ExchangeRate;
import com.exchanger.service.ExchangeRatesService;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() {
        exchangeRatesService = new ExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ExchangeRate> exchangeRates = exchangeRatesService.getAllExchangeRates();
            response.getWriter().write(new Gson().toJson(exchangeRates));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            ExceptionHandler.handle(e, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String baseCurrencyCode = request.getParameter("baseCurrencyCode");
            String targetCurrencyCode = request.getParameter("targetCurrencyCode");
            double rate = Double.parseDouble(request.getParameter("rate"));
            ExchangeRateDto exchangeRateDto = new ExchangeRateDto(baseCurrencyCode, targetCurrencyCode, rate);
            ExchangeRate exchangeRate = exchangeRatesService.setExchangeRate(exchangeRateDto);
            response.getWriter().write(new Gson().toJson(exchangeRate));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            ExceptionHandler.handle(e, response);
        }
    }
}

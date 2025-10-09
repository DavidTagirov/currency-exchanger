package com.exchanger.servlet;

import com.exchanger.dto.CurrencyDto;
import com.exchanger.exception.CurrencyAlreadyExistsException;
import com.exchanger.exception.ExceptionHandler;
import com.exchanger.model.Currency;
import com.exchanger.service.CurrenciesService;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrenciesService currenciesService;

    @Override
    public void init() {
        currenciesService = new CurrenciesService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Currency> currencies = currenciesService.getAllCurrencies();
            response.getWriter().write(new Gson().toJson(currencies));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            ExceptionHandler.handle(e, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String code = request.getParameter("code");
            String fullName = request.getParameter("fullName");
            String sign = request.getParameter("sign");
            CurrencyDto currencyDto = new CurrencyDto(code, fullName, sign);
            currenciesService.setCurrency(currencyDto);
            response.getWriter().write(new Gson().toJson(currencyDto));
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            ExceptionHandler.handle(e, response);
        }
    }
}

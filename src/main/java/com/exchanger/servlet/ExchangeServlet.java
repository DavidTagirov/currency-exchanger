package com.exchanger.servlet;

import com.exchanger.dto.ExchangeResponseDto;
import com.exchanger.dto.ExchangeRequestDto;
import com.exchanger.exception.ExceptionHandler;
import com.exchanger.service.ExchangeRatesService;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() {
        exchangeRatesService = new ExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String fromCurrencyCode = request.getParameter("from");
            String toCurrencyCode = request.getParameter("to");
            double amount = Double.parseDouble(request.getParameter("amount"));
            ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(fromCurrencyCode, toCurrencyCode, amount);
            ExchangeResponseDto exchangeResponseDto = exchangeRatesService.exchange(exchangeRequestDto);
            response.getWriter().write(new Gson().toJson(exchangeResponseDto));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            ExceptionHandler.handle(e, response);
        }
    }
}

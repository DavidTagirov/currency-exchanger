package com.exchanger.controller;

import com.exchanger.service.CurrenciesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrenciesService currenciesService;

    @Override
    public void init() {
        currenciesService = new CurrenciesService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String currencies = currenciesService.getAllCurrencies();
            response.getWriter().write(currencies);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}

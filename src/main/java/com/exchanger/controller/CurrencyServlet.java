package com.exchanger.controller;

import com.exchanger.exceptions.CurrencyNotFoundException;
import com.exchanger.model.Currency;
import com.exchanger.service.CurrenciesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.InvalidPathException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrenciesService currencyService;

    @Override
    public void init() {
        currencyService = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            String code = request.getPathInfo();
            String currency = currencyService.getCurrency(code);
            response.getWriter().write(currency);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (InvalidPathException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (CurrencyNotFoundException e){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

package org.feup.apm.stockservice;
import org.feup.apm.stockservice.Person;

interface IStockQuoteService
{
    String getQuote(in String ticker,in Person requester);
}
package com.example.stelian.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stelian on 06.04.2015.
 */
public class CurrencyCreator {

    private List<String> types;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public CurrencyCreator() {
        this.types = new ArrayList<String>();

        this.createTypes();
    }

    /**
     * add the currencies one by one into the list
     * the list will be retrieved by the MainActivity class so that the items will be displayed int he spinner
     */
    private void createTypes() {

        this.getTypes().add("...");
        this.getTypes().add("AUD");
        this.getTypes().add("BGN");
        this.getTypes().add("BRL");
        this.getTypes().add("CAD");
        this.getTypes().add("CHF");
        this.getTypes().add("CNY");
        this.getTypes().add("CZK");
        this.getTypes().add("DKK");
        this.getTypes().add("GBP");
        this.getTypes().add("HKD");
        this.getTypes().add("HRK");
        this.getTypes().add("HUF");
        this.getTypes().add("IDR");
        this.getTypes().add("ILS");
        this.getTypes().add("INR");
        this.getTypes().add("JPY");
        this.getTypes().add("KRW");
        this.getTypes().add("MXN");
        this.getTypes().add("MYR");
        this.getTypes().add("NOK");
        this.getTypes().add("NZD");
        this.getTypes().add("PHP");
        this.getTypes().add("PLN");
        this.getTypes().add("RON");
        this.getTypes().add("RUB");
        this.getTypes().add("SEK");
        this.getTypes().add("SGD");
        this.getTypes().add("THB");
        this.getTypes().add("TRY");
        this.getTypes().add("USD");
        this.getTypes().add("ZAR");
    }
}

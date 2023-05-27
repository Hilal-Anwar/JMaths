package org.jmath.jconvert;

import org.jmath.jconvert.currency.CurrencyLoader;
import org.jmath.jconvert.quantities.Currencies;
import org.jmath.jconvert.quantities.Metric;
import org.jmath.jconvert.quantities.Temperature;

import java.util.Currency;
import java.util.TreeMap;

public class JConverter {
    private final CurrencyLoader currencyLoader = new CurrencyLoader();

    public JConverter() {
        new Thread(currencyLoader::reload).start();
    }

    public TreeMap<String, String> getAllCurrency() {
        Currencies[] value = Currencies.values();
        TreeMap<String, String> allCurrency = new TreeMap<>();
        for (Currencies currencies : value) {
            try {
                if (Currency.getAvailableCurrencies().contains(Currency.getInstance(currencies.toString()))) {
                    allCurrency.put(Currency.getInstance(currencies.toString()).getDisplayName(), Currency.
                            getInstance(currencies.toString()).getCurrencyCode());
                }
            } catch (Exception e) {
                allCurrency.put(currencies.toString(), currencies.toString());
            }

        }
        return allCurrency;
    }

    public Object[] getAllCurrencyName() {
        return getAllCurrency().keySet().toArray();
    }

    public double convertTo(double amount, Currencies currencies1, Currencies currencies2) {
        double x = currencyLoader.getCurrencyValue(currencies1.getV());
        double y = currencyLoader.getCurrencyValue(currencies2.getV());
        return amount * y / x;
    }

    public <T extends Conversion> double convertTo(double value, T unit1, T unit2) {
        double x = 0, y = 0;
        if (!(unit1 instanceof Temperature)) {
            //try {
            x = (double) unit1.getV();
            //getMethod("getV").invoke(unit1);
            y = (double) unit2.getV();
            //getMethod("getV").invoke(unit2);
            //} catch (IllegalAccessException |
            //InvocationTargetException | NoSuchMethodException e) {
            //    e.printStackTrace();
            //}
            return value * y / x;
        } else return convertTo(value,
                (Temperature) unit1, (Temperature) unit2);
    }

    public <T extends Conversion> double convertTo(double value, Metric u1, T unit1, Metric u2, T unit2) {
        double x = 0, y = 0;
        if (!(unit1 instanceof Temperature)) {
            //try {
            x = (double) unit1.getV();
            //getDeclaringClass().getMethod("getV").invoke(unit1);
            y = (double) unit2.getV();
            //getDeclaringClass().getMethod("getV").invoke(unit2);
            //} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            //e.printStackTrace();
            // }
            return value * (u2.getV() * y) / (u1.getV() * x);
        } else return u2.getV() / u1.getV() * convertTo(value, (Temperature) unit1, (Temperature) unit2);
    }

    private double convertTo(double value, Temperature o1, Temperature o2) {
        if (o1.equals(Temperature.degree_Celsius) && o2.equals(Temperature.kelvin))
            return 273 + value;
        else if (o1.equals(Temperature.kelvin) && o2.equals(Temperature.degree_Celsius))
            return value - 273;
        else if (o1.equals(Temperature.degree_Celsius) && o2.equals(Temperature.degree_Fahrenheit))
            return (value) * 9 / 5 + 32;
        else if (o1.equals(Temperature.degree_Fahrenheit) && o2.equals(Temperature.degree_Celsius))
            return (value - 32) * 5 / 9;
        else if (o1.equals(Temperature.kelvin) && o2.equals(Temperature.degree_Fahrenheit))
            return (value) * 9 / 5 + 32 + 273;
        else if (o1.equals(Temperature.degree_Fahrenheit) && o2.equals(Temperature.kelvin))
            return (value - 273) * 9 / 5 + 32;
        else if (o1.equals(Temperature.degree_Celsius) && o2.equals(Temperature.degree_Rankine))
            return (value + 273.15) * 9 / 5;
        else if (o1.equals(Temperature.degree_Rankine) && o2.equals(Temperature.degree_Celsius))
            return (value * 9 / 5) - 273.15;
        else if (o1.equals(Temperature.degree_Fahrenheit) && o2.equals(Temperature.degree_Rankine))
            return (((value - 32) * 5 / 9) + 273.15) * 9 / 5;
        else if (o1.equals(Temperature.degree_Rankine) && o2.equals(Temperature.degree_Fahrenheit))
            return (((value * 5 / 9) - 273.15) * 9 / 5) + 32;
        else if (o1.equals(Temperature.kelvin) && o2.equals(Temperature.degree_Rankine))
            return value * 1.8;
        else if (o1.equals(Temperature.degree_Rankine) && o2.equals(Temperature.kelvin))
            return value / 1.8;
        return value;
    }

    public void reloadCurrency() {
        new Thread(currencyLoader::reload).start();
    }
}

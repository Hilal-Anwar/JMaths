package org.jmath.jconvert;

import org.jmath.jconvert.currency.CurrencyDataLoader;

import org.jmath.jconvert.quantities.Currencies;
import org.jmath.jconvert.quantities.Metric;
import org.jmath.jconvert.quantities.Temperature;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Currency;
import java.util.TreeMap;

public class JConverter {
    private final CurrencyDataLoader currencyLoader = new CurrencyDataLoader();

    public JConverter() {
        new Thread(() -> {
            try {
                currencyLoader.reload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
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

    public <T extends Enum<T>> double convertTo(double value, T unit1, T unit2) {
        double m1 = 0, m2 = 0, c1 = 0, c2 = 0;
        try {
            m1 = (double) unit1.getClass().getMethod("getV").invoke(unit1);
            m2 = (double) unit2.getClass().getMethod("getV").invoke(unit2);

            c1 = (double) unit1.getClass().getMethod("getC").invoke(unit1);
            c2 = (double) unit2.getClass().getMethod("getC").invoke(unit2);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return (value * m1 + c1 - c2) / m2;
    }

    public <T extends Enum<T>> double convertTo(double value, Metric u1, T unit1, Metric u2, T unit2) {
        if (unit1 instanceof Currencies)
            return convertTo(value, unit1, unit2);
        return convertTo(value * u1.getV(), unit1, unit2) / u2.getV();
    }
    public void reloadCurrency() {
        new Thread(() -> {
            try {
                currencyLoader.reload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}

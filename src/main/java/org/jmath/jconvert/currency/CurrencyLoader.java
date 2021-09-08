package org.jmath.jconvert.currency;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.StringTokenizer;

public class CurrencyLoader
{
    final private Database database = new Database("currency");
    private final HttpClient client = HttpClient.newHttpClient();
    public void reload()
    {
        String CURRENCY_URL = "https://open.er-api.com/v6/latest/USD";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CURRENCY_URL)).
                        build();
        HttpResponse<String> httpResponse;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            StringTokenizer stringTokenizer = new StringTokenizer(httpResponse.body(), "{}");
            String data = "";
            while (stringTokenizer.countTokens() >= 1) {
                data = stringTokenizer.nextToken();

            }
            stringTokenizer = new StringTokenizer(data, ",");
            String name;
            while (stringTokenizer.hasMoreTokens()) {
                name = stringTokenizer.nextToken();
                database.setData(name.substring(1, name.lastIndexOf('"')), name.substring(name.indexOf(':') + 1));
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Check your connection if you are using it for first time");
            System.err.println("If you are connected to internet most recent data is used for conversion");
        }
    }

    public double getCurrencyValue(String code) {
        if (database.getData(code)!=null)
        return Double.parseDouble(database.getData(code));
        return Double.NaN;
    }

}

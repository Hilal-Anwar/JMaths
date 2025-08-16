package org.jmath.jconvert.currency;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class CurrencyDataLoader
{
    public void reload() throws IOException {
        try {
            JsonElement root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("currency_name.json")));
            System.out.println(root.getAsJsonObject().asMap());
        } catch (FileNotFoundException e) {
            try {
                String CURRENCY_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies.json";
                FileUtils.copyURLToFile(
                        URI.create(CURRENCY_URL).toURL(),
                        new File("currency_name.json"),
                        300,
                        500);
                JsonElement root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("currency_name.json")));
                System.out.println(root.getAsJsonObject().getAsJsonObject("inr").asMap());
            }
            catch (Exception w){
                String CURRENCY_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/inr.json";
                FileUtils.copyURLToFile(
                        URI.create(CURRENCY_URL).toURL(),
                        new File("currency.json"),
                        300,
                        500);
                JsonElement root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("currency.json")));
                System.out.println(root.getAsJsonObject().getAsJsonObject("inr").asMap());
            }
        }

        /*String CURRENCY_URL = "https://open.er-api.com/v6/latest/USD";
        URL url = new URL(CURRENCY_URL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));*/

        /*
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
        }*/
    }

   /* public double getCurrencyValue(String code) {
        if (database.getData(code)!=null)
         return Double.parseDouble(database.getData(code));
        return Double.NaN;
    }*/
   public static void main(String[] args) {
       CurrencyDataLoader currencyDataLoader=new CurrencyDataLoader();
       try {
           currencyDataLoader.reload();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }

}

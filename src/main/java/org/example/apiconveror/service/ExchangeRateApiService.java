package org.example.apiconveror.service;

import com.google.gson.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.example.apiconveror.model.Divisa;
import org.example.apiconveror.model.DivisaExchangeRate;
import org.example.apiconveror.model.TipoDivisa;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExchangeRateApiService {


    public List<Divisa> convertir(String a, String b, Double c) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v6.exchangerate-api." +
                        "com/v6/TuApiKey/latest/" + a + "/"))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        String json = response.body();


        Gson gson = new Gson().newBuilder()
                .setPrettyPrinting()
                .create();

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        // Extraer solo los campos deseados
        String baseCode = jsonObject.get("base_code").getAsString();

        Type mapType = new TypeToken<Map<String, Double>>() {}.getType();
        Map<String, Double> conversionRates = gson.fromJson(jsonObject.get("conversion_rates"), mapType);

        DivisaExchangeRate divisaExchangeRate = new DivisaExchangeRate(baseCode, conversionRates);

/*
        DivisaExchangeRate divisaExchangeRate =
                gson.fromJson(json, DivisaExchangeRate.class);
*/

        //Divisa divisa = new Divisa(divisaExchangeRate);

        Divisa divisa = new Divisa(divisaExchangeRate);
        divisa.setPaisConver(TipoDivisa.valueOf(b));
        divisa.setMonedaOrigen(c);

        Double resultado= c * divisaExchangeRate.conversion_rates().get(a) * divisaExchangeRate.conversion_rates().get(b);
        divisa.setMonedaFinal(resultado);





        return Divisa.addHistorial(divisa);
    }
}
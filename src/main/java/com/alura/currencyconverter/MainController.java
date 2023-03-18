package com.alura.currencyconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    //private final String API_KEY = "";
    private final String API_KEY = "KEY STORAGE RETRIEVAL PLACEHOLDER";
    private final String USER_AGENT_ID = "Java/" + System.getProperty("java.version");
    @FXML
    TextField txtQuantityFrom, txtQuantityTo;
    @FXML
    ComboBox<com.alura.currencyconverter.models.Currency> cmbCurrencyFrom, cmbCurrencyTo;
    @FXML
    Label lblResultFrom, lblResultTo;
    String currencyFrom = "";
    String currencyTo = "";
    private List<com.alura.currencyconverter.models.Currency> currencyList = new ArrayList<>();

    private void convert(String newValue)
    {
        boolean quantityOk = true;
        txtQuantityTo.setText("");
        if (!newValue.matches("\\d*")) {
            txtQuantityFrom.setText(newValue.replaceAll("[^\\d]", ""));
            quantityOk = false;
        }
        if (quantityOk)
        {
            try {
                currencyFrom = cmbCurrencyFrom.getValue().getCode();
                currencyTo = cmbCurrencyTo.getValue().getCode();
                if ( newValue.trim().length() > 0 )
                    rate(Double.valueOf(txtQuantityFrom.getText()), currencyFrom, currencyTo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCurrencies();
        txtQuantityFrom.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("CHANGE -> "  + oldValue + " : " + newValue);
            convert(newValue);
        });
        cmbCurrencyFrom.setItems(FXCollections.observableArrayList(currencyList));
        cmbCurrencyTo.setItems(FXCollections.observableArrayList(currencyList));
        cmbCurrencyFrom.getSelectionModel().select(5);
        cmbCurrencyTo.getSelectionModel().select(7);
        cmbCurrencyFrom.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                convert(txtQuantityFrom.getText());
        });
        cmbCurrencyTo.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                convert(txtQuantityFrom.getText());
        });

    }
    /*private double rate(Currency from, Currency to) throws IOException {
        String queryPath
                = "https://free.currconv.com/api/v7/convert?q="
                + from.getCurrencyCode() + "_"
                + to.getCurrencyCode()
                + "&compact=ultra&apiKey=" + API_KEY;
        URL queryURL = new URL(queryPath);
        HttpURLConnection connection
                = (HttpURLConnection) queryURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT_ID);
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // 200 is HTTP status OK
            InputStream stream
                    = (InputStream) connection.getContent();
            Scanner scanner = new Scanner(stream);
            String quote = scanner.nextLine();
            String number = quote.substring(quote.indexOf(':') + 1,
                    quote.indexOf('}'));
            return Double.parseDouble(number);
        } else {
            String excMsg = "Query " + queryPath
                    + " returned status " + responseCode;
            throw new RuntimeException(excMsg);
        }
    }*/

    private void rate(Double amount, String currencyFrom, String currencyTo) throws IOException {
        String url_str = "https://v6.exchangerate-api.com/v6/49d192415f028f7f7e42ad57/pair/"+ currencyFrom + "/" + currencyTo + "/" + amount;
        URL url = null;
        url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        try {
            request.connect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        Double conversionRate = jsonobj.get("conversion_rate").getAsDouble();
        Double conversionResult = jsonobj.get("conversion_result").getAsDouble();
        System.out.println(amount + " * " +conversionRate + " = " + conversionResult);

        lblResultFrom.setText(amount + " " + cmbCurrencyFrom.getSelectionModel().getSelectedItem().getName() + " is equal to...");
        lblResultTo.setText(conversionResult + " " + cmbCurrencyTo.getSelectionModel().getSelectedItem().getName());
        txtQuantityTo.setText(conversionResult + "");
        /*JsonObject jsonObject = jsonConversionRates.getAsJsonObject();
        String req_result = jsonObject.get(currencyTo).getAsString();
        System.out.println(amount + currencyFrom + " : " + (amount * Double.valueOf(req_result)) + " " + currencyTo);
        txtQuantityTo.setText((amount * Double.valueOf(req_result)) + "");*/

    }

    private void initCurrencies()
    {
        currencyList.add(new com.alura.currencyconverter.models.Currency("BRL", "Brazilian Real", "Brazil"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("CAD", "Canadian Dollar", "Canada"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("EUR", "Euro", "European Union"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("GBP", "Pound Sterling", "United Kingdom"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("JPY", "Japanese Yen", "Japan"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("MXN", "Mexican Peso", "Mexico"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("KRW", "South Korean Won", "South Korea"));
        currencyList.add(new com.alura.currencyconverter.models.Currency("USD", "United States Dollar", "United States"));
    }
}
package com.alura.currencyconverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CurrencyConverterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CurrencyConverterApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        scene.getStylesheets().add(CurrencyConverterApplication.class.getResource("styles.css").toString());
        stage.setTitle("Currency Converter (@niluxerochi)");
        stage.setScene(scene);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
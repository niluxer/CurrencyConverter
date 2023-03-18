module com.alura.currencyconverter {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.gson;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.alura.currencyconverter to javafx.fxml;
    exports com.alura.currencyconverter;
}
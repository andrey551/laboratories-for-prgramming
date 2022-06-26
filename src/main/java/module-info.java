module com.example.lab8_cli {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.lab8_cli to javafx.fxml;
    opens com.example.lab8_cli.controller to javafx.fxml;
    exports com.example.lab8_cli;
    exports com.example.lab8_cli.controller;
}
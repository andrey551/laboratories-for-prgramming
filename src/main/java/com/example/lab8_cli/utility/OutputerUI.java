package com.example.lab8_cli.utility;

import com.example.lab8_cli.controller.tool.ObservableResourceFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.concurrent.atomic.AtomicReference;

public class OutputerUI {
    private static final String INFO_TITLE = "Information";
    private static final String ERROR_TITLE = "Error";

    private static ObservableResourceFactory resourceFactory;

    private static void msg(String title, String toOut, String[] args, Alert.AlertType msgType) {
        Alert alert = new Alert(msgType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(tryResource(toOut, args));
        alert.showAndWait();
    }

    public static String input(Stage stage) {
        AtomicReference<String> res = new AtomicReference<>();
        TilePane r = new TilePane();
        stage.setTitle(tryResource("InputBox",null));
        TextField td = new TextField();
        Button b = new Button(tryResource("Summit", null));
        b.setOnAction((e)->{
            res.set(td.getText());
            stage.close();
        });
        r.getChildren().addAll(td, b);
        Scene sc = new Scene(r, 400, 100);
        stage.setScene(sc);
        stage.showAndWait();

        return res.toString();
    }

    private static String tryResource(String str, String[] args) {
        try{
            if(haveResourceFactory()) throw new NullPointerException();
            if(args == null) return resourceFactory.getResources().getString(str);
            MessageFormat messageFormat = new MessageFormat(resourceFactory.getResources().getString(str));
            return messageFormat.format(args);
        } catch (MissingResourceException|NullPointerException e) {
            return str;
        }
    }

    public static void info(String toOut, String[] args) {
        msg(INFO_TITLE, toOut, args, Alert.AlertType.INFORMATION);
    }

    public static void info(String toOut) {
        info(toOut, null);
    }

    public static void error(String toOut,String[] args) {
        msg(ERROR_TITLE, toOut, args, Alert.AlertType.ERROR);
    }

    public static void error(String toOut) {
        error(toOut, null);
    }

    public static void tryError(String toOut, String[] args) {
        if(toOut.startsWith("Error: ")) {
            msg(ERROR_TITLE, toOut.substring(7), args, Alert.AlertType.ERROR);
        } else {
            msg(INFO_TITLE, toOut, args, Alert.AlertType.INFORMATION);
        }
    }



    public static void tryError(String toOut) {
        tryError(toOut, null);
    }

    public static void setResourceFactory(ObservableResourceFactory resourceFactory) {
        OutputerUI.resourceFactory = resourceFactory;
    }

    public static boolean haveResourceFactory() {
        return resourceFactory == null;
    }
}

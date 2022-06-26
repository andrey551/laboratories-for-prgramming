package com.example.lab8_cli;

import base.Exception.WrongAmountOfElementsException;
import base.Exception.NotDeclaredLimitsException;

import com.example.lab8_cli.controller.AskWindowController;
import com.example.lab8_cli.controller.LoginWindowController;
import com.example.lab8_cli.controller.MainWindowController;
import com.example.lab8_cli.controller.tool.ObservableResourceFactory;
import com.example.lab8_cli.utility.Outputer;
import com.example.lab8_cli.utility.OutputerUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.Scanner;

public class App extends Application{
    public static final String PS1 = ">";
    public static final String PS2 = "$";

    public static final String BUNDLE = "com.example.lab8_cli.bundles.gui";

    private static final String APP_TITLE = "Hello World";

    private static String host;
    private static int port;
    private static Scanner userScanner;
    private static Client client;
    private static ObservableResourceFactory resourceFactory;
    private Stage primaryStage;

    public static void main(String[] args)  {
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResourceBundleObjectProperty(ResourceBundle.getBundle(BUNDLE));
        OutputerUI.setResourceFactory(resourceFactory);
        Outputer.setResourceFactory(resourceFactory);
        if(initialize(args)) launch(args);
        else System.exit(0);
    }

    private static boolean initialize(String[] args) {
        host = "localhost";
        port =  5555;
        return true;
//        try{
//              if(args.length != 2) throw new WrongAmountOfElementsException();
//            host = args[0];
//            port = Integer.parseInt(args[1]);
//            if(port < 0) throw new NotDeclaredLimitsException();
//            return true;
//        } catch (WrongAmountOfElementsException e) {
//            String jarName = new java.io.File(App.class.getProtectionDomain()
//                    .getCodeSource()
//                    .getLocation()
//                    .getPath())
//                    .getName();
//            Outputer.println("Usage: java -jar " + jarName + "<host> <port>" );
//        } catch (NumberFormatException e) {
//            Outputer.printError("PortMustBeNumber");
//        } catch (NotDeclaredLimitsException e) {
//            Outputer.printError("PortMustBeNotNegative");
//        }
//        return false;
    }

    @Override
    public void start(Stage stage) {
        try{
            this.primaryStage = stage;

            FXMLLoader loginWindowLoader = new FXMLLoader();
            loginWindowLoader.setLocation(getClass().getResource("view/LoginWindow.fxml"));
            Parent loginWindowRootNode = loginWindowLoader.load();
            Scene loginWindowScene = new Scene(loginWindowRootNode);
            LoginWindowController loginWindowController = loginWindowLoader.getController();
            loginWindowController.setApp(this);
            loginWindowController.setClient(client);
            loginWindowController.initLangs(resourceFactory);

            primaryStage.setTitle(APP_TITLE);

            primaryStage.setScene(loginWindowScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        userScanner = new Scanner(System.in);
        client = new Client(host, port);
        client.run();
    }

    @Override
    public void stop() {
        client.stop();
        userScanner.close();
    }

    public void setMainWindow() {
        try{
            FXMLLoader mainWindowLoader = new FXMLLoader();
            mainWindowLoader.setLocation(getClass().getResource("view/MainWindow.fxml"));
            Parent mainWindowRootNode = mainWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            MainWindowController mainWindowController = mainWindowLoader.getController();
            mainWindowController.initLangs(resourceFactory);

            FXMLLoader askWindowLoader = new FXMLLoader();
            askWindowLoader.setLocation(getClass().getResource("view/AskWindow.fxml"));
            Parent askWindowRootNode = askWindowLoader.load();
            Scene askWindowScene = new Scene(askWindowRootNode);
            Stage askStage = new Stage();
            askStage.setTitle(APP_TITLE);
            askStage.setScene(askWindowScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskWindowController askWindowController = askWindowLoader.getController();
            askWindowController.setAskStage(askStage);
            askWindowController.initLangs(resourceFactory);

            mainWindowController.setClient(client);
            mainWindowController.setUsername(client.getUsername());
            mainWindowController.setAskStage(askStage);
            mainWindowController.setPrimaryStage(primaryStage);
            mainWindowController.setAskWindowController(askWindowController);


            primaryStage.setScene(mainWindowScene);
            primaryStage.setMinWidth(mainWindowScene.getWidth());
            primaryStage.setMinHeight(mainWindowScene.getHeight());
            primaryStage.setResizable(true);
            mainWindowController.refreshButtonOnAction();
        } catch (Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
        }
    }
}

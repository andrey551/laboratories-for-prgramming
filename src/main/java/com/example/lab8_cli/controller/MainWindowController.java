package com.example.lab8_cli.controller;

import base.Interaction.Response;
import base.Interaction.WrappedVehicle;
import base.Vehicle.FuelType;
import base.Vehicle.Vehicle;
import com.example.lab8_cli.App;
import com.example.lab8_cli.Client;
import com.example.lab8_cli.StatsBundle.*;
import com.example.lab8_cli.controller.tool.ObservableResourceFactory;
import com.example.lab8_cli.utility.OutputerUI;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;

import java.io.File;
import java.io.Serializable;
import javafx.util.Duration;
import java.util.*;

public class MainWindowController {
    public static final String LOGIN_COMMAND_NAME = "login";
    public static final String REGISTER_COMMAND_NAME = "register";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String INFO_COMMAND_NAME = "info";
    public static final String ADD_COMMAND_NAME = "add";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_COMMAND_NAME = "remove_by_id";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String EXIT_COMMAND_NAME = "exit";
    public static final String ADD_IF_MAX_COMMAND_NAME = "add _if_max";
    public static final String REMOVE_GREATER_COMMAND_NAME = "remove_greater";
    public static final String COUNT_LESS_THAN_FUEL_TYPE_COMMAND_NAME = "count_less_than_fuel_type";
    public static final String FILTER_LESS_LESS_THAN_FUEL_CONSUMPTION_TYPE_COMMAND_NAME = "filter_less_than_fuel_consumption";
    public static final String GROUP_COUNTING_BY_CAPACITY_TYPE_COMMAND_NAME = "group_counting_by_capacity";
    private final long RANDOM_SEED = 1821L;
    private final Duration ANIMATION_DURATION = Duration.millis(500);
    private final double MAX_SIZE = 250;

    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, Integer> idColumn;
    @FXML
    private TableColumn<Vehicle, String> ownerColumn;
    @FXML
    private TableColumn<Vehicle, Date> creationDateColumn;
    @FXML
    private TableColumn<Vehicle, String> nameColumn;
    @FXML
    private TableColumn<Vehicle, Double> enginePowerColumn;
    @FXML
    private TableColumn<Vehicle, Long> capacityColumn;
    @FXML
    private TableColumn<Vehicle, Integer> fuelConsumptionColumn;
    @FXML
    private TableColumn<Vehicle, FuelType> fuelTypeColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;
    @FXML
    private Button infoButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button executeScriptButton;
    @FXML
    private TableColumn<Vehicle, Integer> coordinatesXColumn;
    @FXML
    private TableColumn<Vehicle, Double> coordinatesYColumn;
    @FXML
    private Button addIfMaxButton;
    @FXML
    private Button removeGreaterButton;
    @FXML
    private Button countLessThanFuelTypeButton;
    @FXML
    private Button filterLessThanFuelConsumptionButton;
    @FXML
    private Button groupCountingByCapacityButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Tooltip executeButtonTooltip;
    @FXML
    private Tooltip addIfMaxButtonTooltip;
    @FXML
    private Tooltip removeGreaterButtonTooltip;
    @FXML
    private Tooltip countLessThanFuelTypeButtonTooltip;
    @FXML
    private Tooltip filterLessThanFuelConsumptionButtonTooltip;
    @FXML
    private Tooltip groupCountingByCapacityButtonTooltip;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private Stage inputStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Long> shapeMap;
    private Map<Long, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap ;

    public void initialize() {
        initializeTable();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("Germany", new Locale("de", "DE"));
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Hungary", new Locale("hu", "HU"));
        localeMap.put("Spanish", new Locale("es", "EC"));
        localeMap.put("Việt Nam", new Locale("vi", "VI"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOwner().getUsername()));
        creationDateColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory((cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName())));
        coordinatesXColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        enginePowerColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getEnginePower()));
        capacityColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCapacity()));
        fuelConsumptionColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getFuelConsumption()));
        fuelTypeColumn.setCellValueFactory(cellData->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getFuelType()));
    }

    @FXML
    private void bindGuiLanguage() {
//        resourceFactory.setResourceBundleObjectProperty(ResourceBundle.getBundle(
//                App.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())
//        ));
        String res = languageComboBox.getSelectionModel().getSelectedItem();
        switch(res) {
            case "Germany":
                resourceFactory.setResourceBundleObjectProperty(new StatsBundle_de_DE());
                break;
            case "Русский":
                resourceFactory.setResourceBundleObjectProperty(new StatsBundle_ru_RU());
                break;
            case "Hungary":
                resourceFactory.setResourceBundleObjectProperty(new StatsBundle_hu_HU());
                break;
            case "Spanish":
                resourceFactory.setResourceBundleObjectProperty(new StatsBundle_es_EC());
                break;
            case "Việt Nam":
                resourceFactory.setResourceBundleObjectProperty(new StatsBundle_vi_VN());
                break;
            default:
                resourceFactory.setResourceBundleObjectProperty(new StatsBundle());
                break;
        }


        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        enginePowerColumn.textProperty().bind(resourceFactory.getStringBinding("EnginePowerColumn"));
        capacityColumn.textProperty().bind(resourceFactory.getStringBinding("CapacityColumn"));
        fuelConsumptionColumn.textProperty().bind(resourceFactory.getStringBinding("FuelConsumptionColumn"));
        fuelTypeColumn.textProperty().bind(resourceFactory.getStringBinding("FuelTypeColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        addButton.textProperty().bind((resourceFactory.getStringBinding("AddButton")));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        executeScriptButton.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButton"));
        addIfMaxButton.textProperty().bind(resourceFactory.getStringBinding("AddIfMaxButton"));
        removeGreaterButton.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButton"));
        countLessThanFuelTypeButton.textProperty().bind(resourceFactory.getStringBinding("CountLessThanFuelTypeButton"));
        filterLessThanFuelConsumptionButton.textProperty().bind(resourceFactory.getStringBinding("FilterLessThanFuelConsumptionButton"));
        groupCountingByCapacityButton.textProperty().bind(resourceFactory.getStringBinding("GroupCountingByCapacityButton"));
        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));

        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        executeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ExecuteButtonTooltip"));
        addIfMaxButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddIfMaxButtonTooltip"));
        removeGreaterButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButtonTooltip"));
        countLessThanFuelTypeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("CountLessThanFuelTypeButtonTooltip"));
        filterLessThanFuelConsumptionButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("FilterLessThanFuelConsumptionButtonTooltip"));
        groupCountingByCapacityButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("GroupCountingByCapacityButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
    }

    @FXML
    public void refreshButtonOnAction() {
        requestAction(REFRESH_COMMAND_NAME);
    }

    @FXML
    public void infoButtonOnAction() {
        requestAction(INFO_COMMAND_NAME);

    }

    @FXML
    private void updateButtonOnAction() {
        if(!vehicleTable.getSelectionModel().isEmpty()) {
            Integer id = vehicleTable.getSelectionModel().getSelectedItem().getId();
            askWindowController.setVehicle(vehicleTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            WrappedVehicle wrappedVehicle = askWindowController.getAndClear();
            if(wrappedVehicle != null) requestAction(UPDATE_COMMAND_NAME, id + "", wrappedVehicle);
        } else OutputerUI.error("UpdateButtonSelectionException");
    }



    @FXML
    private void addButtonOnAction() {
        askWindowController.clearVehicle();
        askStage.showAndWait();
        WrappedVehicle wrappedVehicle = askWindowController.getAndClear();
        if(wrappedVehicle != null) requestAction(ADD_COMMAND_NAME, "", wrappedVehicle);
    }

    @FXML
    private void removeButtonOnAction() {
        if(!vehicleTable.getSelectionModel().isEmpty())
            requestAction(REMOVE_COMMAND_NAME, vehicleTable.getSelectionModel().getSelectedItem().getId().toString(), null);
        else OutputerUI.error("RemoveButtonSelectionException");
    }

    @FXML
    private void clearButtonOnAction() {
        requestAction(CLEAR_COMMAND_NAME);
    }

    @FXML
    private void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile == null) return;
        if(client.processScriptToServer(selectedFile)) Platform.exit();
        else refreshButtonOnAction();
    }

    @FXML
    private void addIfMaxButtonOnAction() {
        askWindowController.clearVehicle();
        askStage.showAndWait();
        WrappedVehicle wrappedVehicle = askWindowController.getAndClear();
        if(wrappedVehicle != null) requestAction(ADD_IF_MAX_COMMAND_NAME, "", wrappedVehicle);
    }

    @FXML
    private void removeGreaterButtonOnAction() {
        if(!vehicleTable.getSelectionModel().isEmpty()) {
            Vehicle vehicleFromTable = vehicleTable.getSelectionModel().getSelectedItem();
            WrappedVehicle wrappedVehicle = new WrappedVehicle(
                    vehicleFromTable.getName(),
                    vehicleFromTable.getCoordinates(),
                    vehicleFromTable.getEnginePower(),
                    vehicleFromTable.getCapacity(),
                    vehicleFromTable.getFuelConsumption(),
                    vehicleFromTable.getFuelType()
            );
            requestAction(REMOVE_COMMAND_NAME, "", wrappedVehicle);
        } else OutputerUI.error("RemoveGreaterButtonSelectionException");
    }

    @FXML
    private void countLessThanFuelTypeButtonOnAction() {
        if(vehicleTable.getSelectionModel().isEmpty()) {
            inputStage = new Stage();
            requestAction(COUNT_LESS_THAN_FUEL_TYPE_COMMAND_NAME,OutputerUI.input(inputStage));
        } else OutputerUI.error("CountLessThanFuelTypeButtonException");
    }

    @FXML
    private void filterLessThanFuelConsumptionButtonOnAction() {
            inputStage = new Stage();
            requestAction(FILTER_LESS_LESS_THAN_FUEL_CONSUMPTION_TYPE_COMMAND_NAME, OutputerUI.input(inputStage));
    }

    @FXML
    private void groupCountingByCapacityButtonOnAction() {
        if(vehicleTable.getSelectionModel().isEmpty()) {
            requestAction(GROUP_COUNTING_BY_CAPACITY_TYPE_COMMAND_NAME);
        } else OutputerUI.error("GroupCountingByCapacityButtonException");
    }


    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        Response response = client.processRequestToServer(
                commandName,
                commandStringArgument,
                commandObjectArgument);
        ArrayDeque<Vehicle> responseVehicle  = response.getVehiclesCollection();
        if(commandName.equals("group_counting_by_capacity")) OutputerUI.info(response.getBody());
        if(responseVehicle != null) {
            ObservableList<Vehicle> vehiclesList = FXCollections.observableArrayList(responseVehicle);
            vehicleTable.setItems(vehiclesList);
            TableFilter.forTableView(vehicleTable).apply();
            vehicleTable.getSelectionModel().clearSelection();
            refreshCanvas();
        }
    }

    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }
    private void requestAction(String commandName,String arg){
        requestAction(commandName, arg, null);
    }

    private void refreshCanvas() {
        shapeMap.keySet().forEach(s->canvasPane.getChildren().remove(s));
        shapeMap.clear();
        textMap.values().forEach(s->canvasPane.getChildren().remove(s));
        textMap.clear();
        for(Vehicle vehicle : vehicleTable.getItems()) {
            if(!userColorMap.containsKey(vehicle.getOwner().getUsername()))
                userColorMap.put(vehicle.getOwner().getUsername(),
                                Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));

            double size = Math.min(vehicle.getEnginePower(), MAX_SIZE);

            Shape circleObject = new Circle(size, userColorMap.get(vehicle.getOwner().getUsername()));
            circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
            circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(vehicle.getCoordinates().getX()));
            circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(vehicle.getCoordinates().getY()));

            Text textObject = new Text(vehicle.getId().toString());
            textObject.setOnMouseClicked(circleObject::fireEvent);
            textObject.setFont(Font.font(size/3));
            textObject.setFill(userColorMap.get(vehicle.getOwner().getUsername()).darker());
            textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
            textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

            canvasPane.getChildren().add(circleObject);
            canvasPane.getChildren().add(textObject);
            shapeMap.put(circleObject, vehicle.getId().longValue());
            textMap.put(vehicle.getId().longValue(), textObject);

            ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
            ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
            circleAnimation.setFromX(0);
            circleAnimation.setToX(1);
            circleAnimation.setFromY(0);
            circleAnimation.setToY(1);
            textAnimation.setFromX(0);
            textAnimation.setToX(1);
            textAnimation.setFromY(0);
            textAnimation.setToY(1);
            circleAnimation.play();
            textAnimation.play();
        }
    }

    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        Long id = shapeMap.get(shape);
        for(Vehicle vehicle : vehicleTable.getItems()) {
            if(vehicle.getId() - id == 0) {
                vehicleTable.getSelectionModel().select(vehicle);
                break;
            }
        }
        if(prevClicked != null) {
            prevClicked.setFill(prevColor.brighter());
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage stage) {
        this.askStage = stage;
    }

    public void setInputStage(Stage stage) { this.inputStage = stage;}

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        try {
            this.resourceFactory = resourceFactory;
            for (String localeName : localeMap.keySet()) {
                if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale())) {
                    languageComboBox.getSelectionModel().select(localeName);
                }
            }
            if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty())
                languageComboBox.getSelectionModel().selectFirst();
            languageComboBox.setOnAction(((event) -> {
                        resourceFactory.setResourceBundleObjectProperty(
                                ResourceBundle.getBundle
                                        (App.BUNDLE, localeMap.get(languageComboBox.getValue())));
                    })

            );
            bindGuiLanguage();
        } catch (NullPointerException e) {
           // e.printStackTrace();
        }
    }
}

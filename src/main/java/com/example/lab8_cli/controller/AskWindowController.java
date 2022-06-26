package com.example.lab8_cli.controller;

import base.Exception.MustBeNotEmptyException;
import base.Exception.coordinatesInputException;
import base.Interaction.WrappedVehicle;
import base.Vehicle.Coordinates;
import base.Vehicle.FuelType;
import base.Vehicle.Vehicle;
import com.example.lab8_cli.controller.tool.ObservableResourceFactory;
import com.example.lab8_cli.utility.OutputerUI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AskWindowController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label enginePowerLabel;
    @FXML
    private Label capacityLabel;
    @FXML
    private Label fuelConsumptionLabel;
    @FXML
    private Label fuelTypeLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField enginePowerField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField fuelConsumptionField;
    @FXML
    private ComboBox<FuelType> fuelTypeBox;
    @FXML
    private Button enterButton;

    private Stage askStage;
    private WrappedVehicle resultVehicle;
    private ObservableResourceFactory resourceFactory;

    public void initialize() {
        fuelTypeBox.setItems(FXCollections.observableArrayList(FuelType.values()));
    }

    @FXML
    private void enterButtonOnAction() {
        try{
            resultVehicle = new WrappedVehicle(
                convertName(),
                new Coordinates(
                        convertCoordinatesX(),
                        convertCoordinatesY()),
                convertEnginePower(),
                convertCapacity(),
                convertFuelConsumption(),
                convertFuelType()
            );
            askStage.close();
        } catch(IllegalArgumentException e) {

        } catch( coordinatesInputException e) {
            OutputerUI.info("CoordinatesException");
        }
    }

    private void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        enginePowerLabel.textProperty().bind(resourceFactory.getStringBinding("EnginePowerColumn"));
        capacityLabel.textProperty().bind(resourceFactory.getStringBinding("CapacityColumn"));
        fuelConsumptionLabel.textProperty().bind(resourceFactory.getStringBinding("FuelConsumptionColumn"));
        fuelTypeLabel.textProperty().bind(resourceFactory.getStringBinding("FuelTypeColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    private String convertName() throws IllegalArgumentException{
        String name;
        try{
            name = nameField.getText();
            if(name.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException e) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }

        return name;
    }

    private Integer convertCoordinatesX() throws IllegalArgumentException {
        String strX;
        Integer x;
        try{
            strX = coordinatesXField.getText();
            x = Integer.parseInt(strX);
        } catch ( NumberFormatException e) {
            OutputerUI.error("CoordinatesXFormatException");
            throw new IllegalArgumentException();
        }

        return x;
    }

    private double convertCoordinatesY() throws IllegalArgumentException {
        String strY;
        double y;
        try{
            strY = coordinatesYField.getText();
            y = Double.parseDouble(strY);
        } catch(NumberFormatException e) {
            OutputerUI.error("CoordinatesYFormatException");
            throw new IllegalArgumentException();
        }

        return y;
    }

    private Double convertEnginePower() throws IllegalArgumentException {
        String ePStr ;
        Double enginePower;
        try{
            ePStr = enginePowerField.getText();
            enginePower = Double.parseDouble(ePStr);
        } catch( NumberFormatException e) {
            OutputerUI.error("EnginePowerException");
            throw new IllegalArgumentException();
        }

        return enginePower;
    }

    private Long convertCapacity() throws IllegalArgumentException {
        String capStr ;
        Long capacity;
        try{
            capStr = capacityField.getText();
            capacity = Long.parseLong(capStr);
        } catch (NumberFormatException e) {
            OutputerUI.error("CapacityException");
            throw new IllegalArgumentException();
        }

        return capacity;

    }

    private int convertFuelConsumption() throws IllegalArgumentException {
        String fuelConsStr;
        int fuelConsumption;
        try{
            fuelConsStr = fuelConsumptionField.getText();
            fuelConsumption = Integer.parseInt(fuelConsStr);
        } catch(NumberFormatException e) {
            OutputerUI.error("FuelConsumptionException");
            throw new IllegalArgumentException();
        }
        return fuelConsumption;
    }

    private FuelType convertFuelType() throws IllegalArgumentException {
        String fuelTypeStr;
        FuelType fuelType;
        try{
            fuelTypeStr = fuelTypeBox.getValue().toString();
            if(fuelTypeStr.equals("")) throw new MustBeNotEmptyException();
            fuelType = FuelType.valueOf(fuelTypeStr);
        } catch (MustBeNotEmptyException e) {
            OutputerUI.error("FuelTypeException");
            throw new IllegalArgumentException();
        }
        return fuelType;
    }

    public void setVehicle(Vehicle vehicle) {
        nameField.setText(vehicle.getName());
        coordinatesXField.setText(vehicle.getCoordinates().getX() + "");
        coordinatesYField.setText(vehicle.getCoordinates().getY() + "");
        enginePowerField.setText(vehicle.getEnginePower() + "");
        capacityField.setText(vehicle.getCapacity() + "");
        fuelConsumptionField.setText(vehicle.getFuelConsumption() + "");
        fuelTypeBox.setValue(vehicle.getFuelType());
    }

    public void clearVehicle() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        enginePowerField.clear();
        capacityField.clear();
        fuelConsumptionField.clear();
        fuelTypeBox.setValue(FuelType.ELECTRICITY);
    }

    public WrappedVehicle getAndClear() {
        WrappedVehicle vehicleToReturn = resultVehicle;
        resultVehicle = null;
        return vehicleToReturn;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }


}

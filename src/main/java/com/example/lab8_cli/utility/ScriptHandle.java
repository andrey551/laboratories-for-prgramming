package com.example.lab8_cli.utility;

import base.Exception.*;
import base.Interaction.Request;
import base.Interaction.ResponseStatus;
import base.Interaction.User;
import base.Interaction.WrappedVehicle;
import base.Vehicle.Coordinates;
import base.Vehicle.FuelType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class ScriptHandle {
    public static final String PS1 = ">";
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public ScriptHandle(File scriptFile) {
        try{
            userScanner = new Scanner(scriptFile);
            scannerStack.add(userScanner);
            scriptStack.add(scriptFile);
        } catch (FileNotFoundException e) {

        }
    }

    public Request handle(ResponseStatus serverResponseStatus, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try{
            do {
                try{
                    if(serverResponseStatus == ResponseStatus.ERROR||serverResponseStatus == ResponseStatus.SERVER_EXIT) {
                        throw new IncorrectInputScriptException();
                    }
                    while(!scannerStack.isEmpty() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        if(!scannerStack.isEmpty()) scriptStack.pop();
                        else return null;
                    }
                    userInput = userScanner.nextLine();
                    if(!userInput.isEmpty()) {
                        Outputer.print(PS1);
                        Outputer.println(userInput);
                    }

                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch(NoSuchElementException | IllegalStateException | IncorrectInputScriptException e) {
                    Outputer.println();
                    Outputer.printError("Error typing command!");
                    userCommand = new String[]{"", ""};
                    ++rewriteAttempts;
                    if(rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printError("RewriteAttemptException");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while( userCommand[0].isEmpty());
            try{
                if(serverResponseStatus == ResponseStatus.ERROR || processingCode == ProcessingCode.ERROR)
                    throw new IncorrectInputScriptException();
                switch(processingCode){
                    case OBJECT:
                        WrappedVehicle wrappedVehicle = generateVehicleAdd();
                        return new Request(userCommand[0], userCommand[1], wrappedVehicle, user);
                    case UPDATE_OBJECT:
                        WrappedVehicle wrappedVehicleUpdate = generateVehicleUpdate();
                        return new Request(userCommand[0],userCommand[1], wrappedVehicleUpdate, user );
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if(!scriptFile.exists()) throw new FileNotFoundException();
                        if(!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.printError("ScriptRunning " + scriptFile.getName());
                        break;
                }
            }catch (FileNotFoundException e) {
                //serverOutput.printError("File not found!");
                throw new IncorrectInputScriptException();
            } catch (ScriptRecursionException e) {
                Outputer.printError("ScriptRecursionException");
                throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException e) {
            }
        } catch(IncorrectInputScriptException | IncorrectInputInScriptException e) {
            Outputer.printError("IncorrectInputInScriptException");
            while(!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }

            scriptStack.clear();
            return null;
        }

        return new Request(userCommand[0], userCommand[1],null, user);
    }

    private ProcessingCode processCommand(String command, String commandArgument) {
        try{
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                case "info":
                case "show":
                case "clear":
                case "remove_first":
                case "exit":
                case "group_counting_by_capacity":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                case "add_if_max":
                case "remove_greater":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if(commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if(commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                case "execute_script":
                    if(commandArgument.isEmpty()) throw new CommandUsageException();
                    return ProcessingCode.SCRIPT;
                case "count_less_than_fuel_type":
                    if(commandArgument.isEmpty()) throw new CommandUsageException("<Fuel Type>");
                    break;
                case "filter_less_than_fuel_consumption":
                    if(commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("CommandNotFoundException", command);
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException e) {
            if(e.getMessage() != null) command += " " +e.getMessage();
            Outputer.println("Using",command);
            return ProcessingCode.ERROR;
        }

        return ProcessingCode.OK;
    }

    private WrappedVehicle generateVehicleAdd() throws IncorrectInputScriptException, MustBeNotEmptyException {
        VehicleAsker vehicleAsker = new VehicleAsker(userScanner);
        return new WrappedVehicle(
                vehicleAsker.askName(),
                vehicleAsker.askCoordinates(),
                vehicleAsker.askEnginePower(),
                vehicleAsker.askCapacity(),
                vehicleAsker.askFuelConsumption(),
                vehicleAsker.askFuelType()
        );
    }

    private WrappedVehicle generateVehicleUpdate() throws IncorrectInputScriptException, MustBeNotEmptyException {
        VehicleAsker vehicleAsker = new VehicleAsker(userScanner);
        String name = vehicleAsker.askQuestion("ChangeNameQuestion")?
                vehicleAsker.askName():null;
        Coordinates coordinates = vehicleAsker.askQuestion("ChangeCoordinatesQuestion")?
                vehicleAsker.askCoordinates():null;
        Double enginePower = vehicleAsker.askQuestion("ChangeEnginePowerQuestion")?
                vehicleAsker.askEnginePower():null;
        Long capacity = vehicleAsker.askQuestion("ChangeCapacityQuestion")?
                vehicleAsker.askCapacity():null;
        int fuelConsumption = vehicleAsker.askQuestion("ChangeFuelConsumptionQuestion")?
                vehicleAsker.askFuelConsumption():-1;
        FuelType fuelType = vehicleAsker.askQuestion("ChangeFuelTypeQuestion")?
                vehicleAsker.askFuelType():null;

        return new WrappedVehicle(name, coordinates, enginePower, capacity, fuelConsumption, fuelType);
    }

}

package server.utility;

import base.Interaction.*;
import base.Vehicle.Vehicle;

import java.util.NavigableSet;
import java.util.concurrent.RecursiveTask;

public class RequestHandle extends RecursiveTask<Response> {
    private Request request;
    private CommandManager commandManager;
    private VehicleData vehicleData;

    public RequestHandle (CommandManager commandManager, Request request,
                          VehicleData vehicleData) {
        this.commandManager = commandManager;
        this.request = request;
        this.vehicleData = vehicleData;
    }

    private synchronized  ResponseStatus executeCommand(String command,
                                           String commandArgument, Object objArgument, User user) {
        switch (command) {
            case "":
                break;
            case "help":
                if(!commandManager.help(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "info":
                if(!commandManager.info(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "show":
                if(!commandManager.show(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "add":
                if(!commandManager.add(commandArgument, (WrappedVehicle) objArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "update":
                if(!commandManager.update(commandArgument, (WrappedVehicle) objArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "remove_by_id":
                if(!commandManager.removeById(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "clear":
                if(!commandManager.clear(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "execute_script":
                if(!commandManager.executeScript(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "exit":
                if(!commandManager.exit(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "remove_first":
                if(!commandManager.removeFirst(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "add_if_max" :
                if(!commandManager.addIfMax(commandArgument, (WrappedVehicle) objArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "remove_greater":
                if(!commandManager.removeGreater(commandArgument, (WrappedVehicle) objArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "group_counting_by_capacity" :
                if(!commandManager.groupCountingByCapacity(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "count_less_than_fuel_type":
                if(!commandManager.countLessThanFuelType(commandArgument, user))
                    return  ResponseStatus.ERROR;
                break;
            case "filter_less_than_fuel_consumption":
                if(!commandManager.filterLessThanFuelConsumption(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;

            case "login":
                if(!commandManager.login(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "register":
                if(!commandManager.register(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            case "refresh":
                if(!commandManager.refresh(commandArgument, user))
                    return ResponseStatus.ERROR;
                break;
            default:
                ResponseOutput.appendln("Command " + command + " is not defined. Type help for help <(^-^)>" );
        }

        return ResponseStatus.OK;
    }


    @Override
    protected Response compute() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );
        ResponseStatus responseStatus = executeCommand(
                request.getCommandName(),
                request.getCommandArgument(),
                request.getObjectArgument(),
                hashedUser
        );
        vehicleData.loadCollection();
        if(request.getCommandName().equals("filter_less_than_fuel_consumption"))
            return new Response(
                    responseStatus,
                    ResponseOutput.getAndClear(),
                    null,
                    vehicleData.FilterLessThanFuelConsumption(Integer.parseInt(request.getCommandArgument())));
        return new Response(responseStatus, ResponseOutput.getAndClear(),ResponseOutput.getArgsAndClear(), vehicleData.getCollection());
    }
}

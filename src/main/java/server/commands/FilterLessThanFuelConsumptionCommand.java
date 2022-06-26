package server.commands;

import base.Exception.CollectionIsEmptyException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import base.Vehicle.Vehicle;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.util.ArrayDeque;

/**
 * Command 'filter_less_than_fuel_consumption'. Display elements
 * whose fuelConsumption field value is less than the given one
 */
public class FilterLessThanFuelConsumptionCommand extends AbstractCommand{
    private VehicleData vehicleData;

    public FilterLessThanFuelConsumptionCommand(VehicleData vehicleData) {
        super("filter_less_than_fuel_consumption", "<Fuel consumption>","Display elements whose fuelConsumption field value is less than the given one" );
        this.vehicleData = vehicleData;
    }
    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(argument.isEmpty() || obj != null) throw new WrongAmountOfElementsException();
            if(vehicleData.collectionSize() == 0) throw new CollectionIsEmptyException();
            ResponseOutput.appendln("List of vehicle that have fuel consumption that small than the given: ");
            int consumptionValue = Integer.parseInt(argument);
            ArrayDeque<Vehicle> result = vehicleData.FilterLessThanFuelConsumption(consumptionValue);
            if(result.isEmpty()) ResponseOutput.appendln("none");
            else ResponseOutput.appendln(result);
            
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " " + getUsage());
        } catch (CollectionIsEmptyException e) {
            ResponseOutput.appendln("Collection is empty!");
        } catch (NullPointerException e) {
            ResponseOutput.appendln("<(^-^)>");
        }
        return false;
    }

    
}

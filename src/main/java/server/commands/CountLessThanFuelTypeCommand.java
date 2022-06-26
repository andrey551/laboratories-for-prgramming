package server.commands;

import base.Exception.CollectionIsEmptyException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import base.Vehicle.FuelType;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

/**
 * Command 'count_less_than_fuel_type fuelType'. Print the number of
 * elements whose fuelType field value is less than the given one
 */
public class CountLessThanFuelTypeCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public CountLessThanFuelTypeCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("count_less_than_fuel_type", "{script}",  "Print the number of elements whose fuelType field value is less than the given one");
        this.vehicleData = vehicleData;
        this.databaseCollectionManager = databaseCollectionManager;
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
            ResponseOutput.appendln(vehicleData.CountLessThanFuelType(FuelType.valueOf(argument)));
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " ");
        } catch (CollectionIsEmptyException e) {
            ResponseOutput.appendln("Collection is empty!");
        } catch (IllegalArgumentException e) {
            ResponseOutput.appendln("No such fuel type like that");
            ResponseOutput.appendln("List of fuel type: ELECTRICITY, MANPOWER, ANTIMATTER ");
        }

        return false;
    }
}

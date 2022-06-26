package server.commands;

import base.Exception.DatabaseHandlingException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import base.Interaction.WrappedVehicle;
import base.Vehicle.Vehicle;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.util.Date;

/**
 * Command 'add_if_max'.Add a new element to the collection
 * if its value is greater than the value of the
 * largest element in this collection
 */
public class AddIfMaxCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMaxCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager ) {
        super(
                "add_if_max",
                "{element}",
                "Add a new element to the collection if its " +
                        "value is greater than the value of the largest element in this collection");
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
            if(!argument.isEmpty() || obj == null) throw new WrongAmountOfElementsException();
            WrappedVehicle temp = (WrappedVehicle)obj;
            Vehicle vehicleToAdd = databaseCollectionManager.insertVehicle(temp, user);
            
                if(vehicleData.collectionSize() == 0 || vehicleToAdd.compareTo(vehicleData.getLast()) > 0) {
                    vehicleData.addElement(vehicleToAdd);
                    ResponseOutput.appendln("Add element success!");
                    return true;
                } else ResponseOutput.appendError("Element to add is smaller than max element!");
        } catch(WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " " + getUsage());
        } catch (ClassCastException e) {
            ResponseOutput.appendln("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendError("An error occurred while adding element");
        }
        return false;
    }
    
}

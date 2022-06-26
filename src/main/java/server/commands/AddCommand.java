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
 * Command add. Add a new element to the collection
 */
public class AddCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(VehicleData vehicleData,
                      DatabaseCollectionManager databaseCollectionManager) {
        super("add","{element}",  "Add a new element to the collection");
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
            if(!argument.isEmpty()) throw new WrongAmountOfElementsException();
            WrappedVehicle temp = (WrappedVehicle) obj;
                vehicleData.addElement(databaseCollectionManager.insertVehicle(temp, user));
                ResponseOutput.appendln("Vehicle added success!");
                return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage : " + getName() + " " + getUsage());
        } catch (ClassCastException e) {
            ResponseOutput.appendln("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendln("An error occurred while accessing to database");
        }

        return false;
    }
}

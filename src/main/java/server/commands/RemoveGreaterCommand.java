package server.commands;

import base.Exception.CollectionIsEmptyException;
import base.Exception.VehicleNotFoundException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import base.Interaction.WrappedVehicle;
import base.Vehicle.Vehicle;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.util.Date;

/**
 * Command 'remove_greater'. Remove from the collection all elements greater than the given
 */
public class RemoveGreaterCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater","{element}", "Remove from the collection all elements greater than the given");
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
           if(vehicleData.collectionSize() == 0) throw new CollectionIsEmptyException();
           WrappedVehicle temp = (WrappedVehicle) obj;
           Vehicle vehicleToFind = new Vehicle(
                   vehicleData.generateNextId(),
                   temp.getName(),
                   temp.getCoordinates(),
                   new Date(),
                   temp.getEnginePower(),
                   temp.getCapacity(),
                   temp.getFuelConsumption(),
                   temp.getFuelType(),
                   user
           );

           Vehicle vehicleFromCollection = vehicleData.getByValue(vehicleToFind);
           if(vehicleFromCollection == null) throw new VehicleNotFoundException();
           vehicleData.removeGreater(vehicleFromCollection);
           ResponseOutput.appendln("Remove vehicle success!");
            return true;
       } catch (WrongAmountOfElementsException exception) {
           ResponseOutput.appendError("Usage: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
           ResponseOutput.appendError("Collection is empty!");
        } catch (VehicleNotFoundException exception) {
           ResponseOutput.appendError("Not found vehicle such that in collection!");
        }
        return false;
    }
    
}

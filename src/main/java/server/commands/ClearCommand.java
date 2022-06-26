package server.commands;
import base.Exception.DatabaseHandlingException;
import base.Exception.ManualDatabaseEditException;
import base.Exception.PermissionDeniedException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import base.Vehicle.Vehicle;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

/**
 * Command 'clear'. Clear collection
 */
public class ClearCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("clear","", "Clear collection");
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
            if(!argument.isEmpty() || obj != null) throw new WrongAmountOfElementsException();
            for(Vehicle vehicle : vehicleData.getCollection()) {
                if(!vehicle.getOwner().equals(user)) throw new PermissionDeniedException();
                if(!databaseCollectionManager.checkVehicleUserId(vehicle.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            vehicleData.clearCollection();
            ResponseOutput.appendln("Collection is cleared!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getClass() + " ");
        } catch (PermissionDeniedException e) {
            ResponseOutput.appendError("This request is not legal");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendError("An error occurred while clearing");
        } catch (ManualDatabaseEditException e) {
            ResponseOutput.appendln("Restart client!");

        }
        return false;
    }
    
}

package server.commands;

import base.Exception.*;
import base.Interaction.User;
import base.Vehicle.Vehicle;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.lang.NumberFormatException;

/**
 * Command 'remove_by_id'. Delete element from colletion by ID
 */
public class RemoveByIdCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveByIdCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_by_id", "<ID>", "Delete element from collection by ID");
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
            Integer id = Integer.parseInt(argument);
            Vehicle vehicleToRemove = vehicleData.getById(id);
            if(vehicleToRemove == null) throw new WrongIdException();
            if(!vehicleToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if(!databaseCollectionManager.checkVehicleUserId(vehicleToRemove.getId(), user)) throw new ManualDatabaseEditException();
            databaseCollectionManager.deleteVehicleById(id);
            vehicleData.removeFromCollection(vehicleToRemove);
            ResponseOutput.appendln("Deleted vehicle!");
            return true;
        } catch(CollectionIsEmptyException e) {
            ResponseOutput.appendln("Empty collection");
        } catch(WrongAmountOfElementsException e) {
            ResponseOutput.appendError("Usage: " + getName() + " ");
        } catch(NumberFormatException e) {
            ResponseOutput.appendError("ID must be a Integer number");
        } catch (WrongIdException e) {
            ResponseOutput.appendError("ID not found!");
        } catch (PermissionDeniedException e) {
            ResponseOutput.appendError("Permission is denied");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendError("An error occurred while removing vehicle");
        } catch (ManualDatabaseEditException e) {
            ResponseOutput.appendln("Restart client");
        }

        return false;
    }
    
}

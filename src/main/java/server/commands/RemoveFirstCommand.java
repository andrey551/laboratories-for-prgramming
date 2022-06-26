package server.commands;

import base.Exception.CollectionIsEmptyException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;
/**
 * Command 'remove_first'. Remove the first element from the collection
 */
public class RemoveFirstCommand extends AbstractCommand{
    private VehicleData vehicleData;

    public RemoveFirstCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_first", "", "Remove the first element from the collection");
        this.vehicleData = vehicleData;
    }
    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty()|| obj != null) throw new WrongAmountOfElementsException();
            if(vehicleData.collectionSize() == 0) throw new CollectionIsEmptyException();
            vehicleData.removeFirst();

            ResponseOutput.appendln("Remove first element success!");

        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " ");
        } catch (CollectionIsEmptyException e) {
            ResponseOutput.appendln("Collection is Empty!");
        }

        return false;
    }

    
}

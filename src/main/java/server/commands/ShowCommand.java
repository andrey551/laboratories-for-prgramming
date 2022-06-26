package server.commands;

import base.Exception.DatabaseHandlingException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import base.Vehicle.Vehicle;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.util.ArrayDeque;

/**
 * Command 'show'. Show all elements of collection
 */
public class ShowCommand  extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public ShowCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("show", "", "Show all elements of collection");
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
//            int userId = databaseCollectionManager.getUserId(user);
//            if( userId == -1) ResponseOutput.appendln("User is not found");
//             else{
//                ArrayDeque<Vehicle> result = new ArrayDeque<>();
//                for(Vehicle e :vehicleData.getCollection()) {
//                    if(databaseCollectionManager.getUserId(e.getOwner()) == userId) {
//                        result.add(e);
//                    }
//                }
//                ResponseOutput.appendln(result);
//            }
            ResponseOutput.appendln(databaseCollectionManager.getCollection());

            return true;
        } catch(WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " ");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendError("Database cannot load!");
        }
        return false;
    }
    
}

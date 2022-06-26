package server.commands;

import base.Exception.CollectionIsEmptyException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

/**
 * Command 'group_counting_by_capacity'. Group the elements of
 * the collection by the value of the field capacity, display the
 * number of elements in each group
 */
public class GroupCountingBycapacityCommand extends AbstractCommand{
    private VehicleData vehicleData;

    public GroupCountingBycapacityCommand(VehicleData vehicleData) {
        super("group_counting_by_capacity","",  "Group the elements of the collection by the value of the field capacity, display the number of elements in each group");
        this.vehicleData = vehicleData;
    }
    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty()||obj != null) throw new WrongAmountOfElementsException();
            if(vehicleData.collectionSize() == 0) throw new CollectionIsEmptyException();
            ResponseOutput.appendln(vehicleData.groupCountingByCapacity());
            return true;
        } catch(WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " ");
        } catch(CollectionIsEmptyException e) {
            ResponseOutput.appendln("Collection is empty!");
        }

        return true;
    }

    
}

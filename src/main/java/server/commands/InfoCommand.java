package server.commands;

import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.time.LocalDateTime;

/**
 * Command 'info'. Show information about collection
 */
public class InfoCommand extends AbstractCommand {
    private VehicleData vehicleData;

    public InfoCommand(VehicleData vehicleData) {
        super("info","", "Show information about collection");
        this.vehicleData = vehicleData;
    }
    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty()) throw new WrongAmountOfElementsException();
            LocalDateTime lastInitTime = vehicleData.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "initialization has not yet taken place in this session" : 
            lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();
        
            LocalDateTime lastSaveTime = vehicleData.getLastSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "initialization has not yet taken place in this session" : 
            lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

            ResponseOutput.appendln("Detail about Collection:");
            ResponseOutput.appendln("Type: " + vehicleData.collectionType());
            ResponseOutput.appendln("Number of elements: " + vehicleData.collectionSize());
            ResponseOutput.appendln("Last save day: " + lastSaveTimeString);
            ResponseOutput.appendln("Last init day: " + lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " ");
        }
        return false;
    }
    
}

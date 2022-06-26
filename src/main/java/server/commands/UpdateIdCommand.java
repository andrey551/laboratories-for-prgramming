package server.commands;

import base.Exception.*;
import base.Exception.NumberFormatException;
import base.Interaction.User;
import base.Interaction.WrappedVehicle;
import base.Vehicle.Coordinates;
import base.Vehicle.FuelType;
import base.Vehicle.Vehicle;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutput;
import server.utility.VehicleData;

import java.util.Date;

/**
 * Command 'update id'. Update the value of the collection element whose id is equal to the given one
 */
public class UpdateIdCommand extends AbstractCommand{
    private VehicleData vehicleData;
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateIdCommand(VehicleData vehicleData, DatabaseCollectionManager databaseCollectionManager) {
        super("update_id", "<ID>",  "Update the value of the collection element whose id is equal to the given one");
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
           if(argument.isEmpty() || obj == null) throw new WrongAmountOfElementsException();
           if(vehicleData.collectionSize() == 0) throw new CollectionIsEmptyException();

           Integer id = Integer.parseInt(argument);
           if(id <= 0) throw new NumberFormatException();
           Vehicle oldVehicle = vehicleData.getById(id);
           if(oldVehicle == null) throw new VehicleNotFoundException();
            if(!oldVehicle.getOwner().equals(user)) throw new PermissionDeniedException();
            if(!databaseCollectionManager.checkVehicleUserId(oldVehicle.getId(), user)) throw new ManualDatabaseEditException();
           WrappedVehicle temp = (WrappedVehicle) obj;

           databaseCollectionManager.updateVehicleById(id, temp);

           String name = temp.getName() == null ? oldVehicle.getName() : temp.getName();
           Coordinates coordinates = temp.getCoordinates() == null ? oldVehicle.getCoordinates() : temp.getCoordinates();
           Date creationDate = oldVehicle.getCreationDate() ;
           Double enginePower = temp.getEnginePower() == null ? oldVehicle.getEnginePower() : temp.getEnginePower();
           Long capacity = temp.getCapacity() == null ? oldVehicle.getCapacity() : temp.getCapacity();
           int fuelConsumption = temp.getFuelConsumption() == -1 ? oldVehicle.getFuelConsumption() : temp.getFuelConsumption();
           FuelType fuelType = temp.getFuelType() == null ? oldVehicle.getFuelType() : temp.getFuelType();


           vehicleData.updateById(new Vehicle(id, name, coordinates, creationDate, enginePower, capacity, fuelConsumption, fuelType, user));

           ResponseOutput.appendln("Update vehicle successful!");
           return true;
       } catch (WrongAmountOfElementsException e) {
           ResponseOutput.appendln("Usage: " + getName() + " ");
       } catch (CollectionIsEmptyException exception) {
           ResponseOutput.appendError("Collection is empty!");
        } catch (NumberFormatException exception) {
           ResponseOutput.appendError("Id must be a number!");
        } catch (VehicleNotFoundException exception) {
           ResponseOutput.appendError("No such vehicle have this ID!");
        } catch (PermissionDeniedException e) {
           ResponseOutput.appendError("Permission is denied");
       } catch (DatabaseHandlingException e) {
           ResponseOutput.appendError("An error occurred while updating vehicle");
       } catch (ManualDatabaseEditException e) {
           ResponseOutput.appendln("Restart client!");
       }
        return false;
    }
    
}

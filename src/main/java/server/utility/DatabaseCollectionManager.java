package server.utility;

import base.Exception.DatabaseHandlingException;
import base.Interaction.User;
import base.Interaction.WrappedVehicle;
import base.Vehicle.Coordinates;
import base.Vehicle.FuelType;
import base.Vehicle.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DatabaseCollectionManager {
    private final String SELECT_ALL_VEHICLE = "SELECT * FROM " + DatabaseHandle.VEHICLE_TABLE;
    private final String SELECT_VEHICLE_BY_ID = SELECT_ALL_VEHICLE + " WHERE " +
            DatabaseHandle.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_VEHICLE_BY_ID_AND_USER_ID = SELECT_VEHICLE_BY_ID + " AND " +
            DatabaseHandle.VEHICLE_TABLE_USER_ID_COLUMN + " = ? ";
    private final String INSERT_VEHICLE = "INSERT INTO " +
            DatabaseHandle.VEHICLE_TABLE + " (" +
            DatabaseHandle.VEHICLE_TABLE_NAME_COLUMN + ", " +
            DatabaseHandle.VEHICLE_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandle.VEHICLE_TABLE_ENGINE_POWER_COLUMN + ", " +
            DatabaseHandle.VEHICLE_TABLE_CAPACITY_POWER_COLUMN + ", " +
            DatabaseHandle.VEHICLE_TABLE_FUEL_CONSUMPTION_COLUMN + ", " +
            DatabaseHandle.VEHICLE_TABLE_FUEL_TYPE_COLUMN + " , " +
            DatabaseHandle.VEHICLE_TABLE_USER_ID_COLUMN + ") VALUES (?, ? , ?, ?, ?, ?, ?)";
    private final String DELETE_VEHICLE_BY_ID = "DELETE FROM " + DatabaseHandle.VEHICLE_TABLE +
            " WHERE " + DatabaseHandle.VEHICLE_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseHandle.COORDINATES_TABLE +
            " WHERE " + DatabaseHandle.COORDINATES_TABLE_VEHICLE_ID_COLUMN + " = ?";
    private final String UPDATE_VEHICLE_NAME_BY_ID = "UPDATE " + DatabaseHandle.VEHICLE_TABLE + " SET " +
            DatabaseHandle.VEHICLE_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandle.VEHICLE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_VEHICLE_ENGINE_POWER_BY_ID = "UPDATE " + DatabaseHandle.VEHICLE_TABLE  + " SET " +
            DatabaseHandle.VEHICLE_TABLE_ENGINE_POWER_COLUMN + " = ?" + " WHERE " +
            DatabaseHandle.VEHICLE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_VEHICLE_CAPACITY_BY_ID = "UPDATE " +DatabaseHandle.VEHICLE_TABLE +  " SET " +
            DatabaseHandle.VEHICLE_TABLE_CAPACITY_POWER_COLUMN + " = ?" + " WHERE " +
            DatabaseHandle.VEHICLE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_VEHICLE_FUEL_CONSUMPTION_BY_ID = "UPDATE " + DatabaseHandle.VEHICLE_TABLE + " SET " +
            DatabaseHandle.VEHICLE_TABLE_FUEL_CONSUMPTION_COLUMN + " = ?" + " WHERE " +
            DatabaseHandle.VEHICLE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_VEHICLE_FUEL_TYPE_BY_ID = "UPDATE " + DatabaseHandle.VEHICLE_TABLE + " SET " +
            DatabaseHandle.VEHICLE_TABLE_FUEL_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandle.VEHICLE_TABLE_ID_COLUMN + " = ?";

    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandle.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_VEHICLE_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandle.COORDINATES_TABLE_VEHICLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM " + DatabaseHandle.USER_TABLE +
            " WHERE " + DatabaseHandle.USER_TABLE_USERNAME_COLUMN + " = ? " + "AND "
            +DatabaseHandle.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandle.COORDINATES_TABLE + " (" +
            DatabaseHandle.COORDINATES_TABLE_VEHICLE_ID_COLUMN + ", " +
            DatabaseHandle.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandle.COORDINATES_TABLE_Y_COLUMN + ") VALUES ( ?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_VEHICLE_ID = "UPDATE " + DatabaseHandle.COORDINATES_TABLE + " SET " +
            DatabaseHandle.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandle.COORDINATES_TABLE_Y_COLUMN + " = ?" +" WHERE " +
            DatabaseHandle.COORDINATES_TABLE_VEHICLE_ID_COLUMN + " = ?";
    private DatabaseHandle databaseHandle;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandle databaseHandle, DatabaseUserManager databaseUserManager) {
        this.databaseHandle = databaseHandle;
        this.databaseUserManager = databaseUserManager;
    }

    private Vehicle createVehicle(ResultSet resultSet) throws SQLException, ParseException {
        int id = resultSet.getInt(DatabaseHandle.VEHICLE_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandle.VEHICLE_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByVehicleId(id);
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("MSK"));
        Date creationDate = formatter.parse(resultSet.getString(DatabaseHandle.VEHICLE_TABLE_CREATION_DATE_COLUMN));
        Double enginePower = resultSet
                .getDouble(DatabaseHandle.VEHICLE_TABLE_ENGINE_POWER_COLUMN);
        Long capacity = resultSet
                .getLong(DatabaseHandle.VEHICLE_TABLE_CAPACITY_POWER_COLUMN);
        int fuelConsumption = resultSet
                .getInt(DatabaseHandle.VEHICLE_TABLE_FUEL_CONSUMPTION_COLUMN);
        FuelType fuelType = FuelType
                .valueOf(resultSet
                        .getString(DatabaseHandle.VEHICLE_TABLE_FUEL_TYPE_COLUMN));
        User owner = databaseUserManager
                .getUserById(resultSet
                        .getInt(DatabaseHandle.VEHICLE_TABLE_USER_ID_COLUMN));

        return new Vehicle(
                id,
                name,
                coordinates,
                creationDate,
                enginePower,
                capacity,
                fuelConsumption,
                fuelType,
                owner
        );
    }

    public ArrayDeque<Vehicle> getCollection() throws DatabaseHandlingException {
        ArrayDeque<Vehicle> result = new ArrayDeque<>();
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = databaseHandle.getPreparedStatement(SELECT_ALL_VEHICLE, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                result.add(createVehicle(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseHandlingException();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            databaseHandle.closePreparedStatement(preparedStatement);
        }

        return result;
    }

    private Coordinates getCoordinatesByVehicleId(int vehicleId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = databaseHandle.getPreparedStatement(SELECT_COORDINATES_BY_VEHICLE_ID, false);
            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Output.println("Request completed SELECT_COORDINATES_BY_VEHICLE_ID");
            if(resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseHandle.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getDouble(DatabaseHandle.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch(SQLException e) {
            Output.printError("An error occurred while reading SELECT_COORDINATES_BY_VEHICLE_ID");
            throw new SQLException(e);
        } finally {
            databaseHandle.closePreparedStatement(preparedStatement);
        }

        return coordinates;
    }

    public Vehicle insertVehicle(WrappedVehicle wrappedVehicle, User user) throws DatabaseHandlingException {
        Vehicle vehicle;
        PreparedStatement preparedVehicleStatement = null;
        PreparedStatement preparedCoordinatesStatement = null;
        try{
            databaseHandle.setCommitMode();
            databaseHandle.setSavePoint();

            Date creationDate = new Date();

            preparedVehicleStatement = databaseHandle.getPreparedStatement(INSERT_VEHICLE, true);
            preparedCoordinatesStatement = databaseHandle.getPreparedStatement(INSERT_COORDINATES, true);

            preparedVehicleStatement.setString(1, wrappedVehicle.getName());
            preparedVehicleStatement.setString(2, String.valueOf(creationDate));
            preparedVehicleStatement.setString(3, String.valueOf(wrappedVehicle.getEnginePower()));
            preparedVehicleStatement.setString(4,String.valueOf( wrappedVehicle.getCapacity()));
            preparedVehicleStatement.setString(5, String.valueOf(wrappedVehicle.getFuelConsumption()));
            preparedVehicleStatement.setString(6, wrappedVehicle.getFuelType().toString());
            preparedVehicleStatement.setInt(7, databaseUserManager.getUserIdByUserName(user));
            if(preparedVehicleStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generateVehicleKeys = preparedVehicleStatement.getGeneratedKeys();
            int vehicleId;
            if(generateVehicleKeys.next()) {
                vehicleId = generateVehicleKeys.getInt(1);
            } else throw new SQLException();

            Output.println("Request completed INSERT_NAME");

            preparedCoordinatesStatement.setInt(1, vehicleId);
            preparedCoordinatesStatement.setString(2, String.valueOf(wrappedVehicle.getCoordinates().getX()));
            preparedCoordinatesStatement.setString(3, String.valueOf(wrappedVehicle.getCoordinates().getY()));
            if(preparedCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            Output.println("Request completed INSERT_COORDINATES");

            vehicle = new Vehicle(
                    vehicleId,
                    wrappedVehicle.getName(),
                    wrappedVehicle.getCoordinates(),
                    creationDate,
                    wrappedVehicle.getEnginePower(),
                    wrappedVehicle.getCapacity(),
                    wrappedVehicle.getFuelConsumption(),
                    wrappedVehicle.getFuelType(),
                    user
            );

            databaseHandle.commit();
            return vehicle;
        } catch (SQLException e) {
            e.printStackTrace();
            Output.printError("An error occurred while inserting vehicle!");
            databaseHandle.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandle.closePreparedStatement(preparedCoordinatesStatement);
            databaseHandle.closePreparedStatement(preparedVehicleStatement);
            databaseHandle.setNormalMode();
        }
    }

    public void updateVehicleById(int vehicleId, WrappedVehicle wrappedVehicle) throws DatabaseHandlingException {
        PreparedStatement prepareUpdateVehicleNameByIdStatement = null;
        PreparedStatement prepareUpdateVehicleEnginePowerByIdStatement = null;
        PreparedStatement prepareUpdateVehicleCapacityByIdStatement = null;
        PreparedStatement prepareUpdateVehicleConsumptionByIdStatement = null;
        PreparedStatement prepareUpdateVehicleFuelTypeByIdStatement = null;
        PreparedStatement prepareUpdateVehicleCoordinatesByIdStatement = null;
        try {
            databaseHandle.setCommitMode();
            databaseHandle.setSavePoint();

            prepareUpdateVehicleNameByIdStatement = databaseHandle
                    .getPreparedStatement(UPDATE_VEHICLE_NAME_BY_ID, false);
            prepareUpdateVehicleEnginePowerByIdStatement = databaseHandle
                    .getPreparedStatement(UPDATE_VEHICLE_ENGINE_POWER_BY_ID, false);
            prepareUpdateVehicleCapacityByIdStatement = databaseHandle
                    .getPreparedStatement(UPDATE_VEHICLE_CAPACITY_BY_ID, false);
            prepareUpdateVehicleConsumptionByIdStatement = databaseHandle
                    .getPreparedStatement(UPDATE_VEHICLE_FUEL_CONSUMPTION_BY_ID, false);
            prepareUpdateVehicleFuelTypeByIdStatement = databaseHandle
                    .getPreparedStatement(UPDATE_VEHICLE_FUEL_TYPE_BY_ID, false);
            prepareUpdateVehicleCoordinatesByIdStatement = databaseHandle
                    .getPreparedStatement(UPDATE_COORDINATES_BY_VEHICLE_ID, false);

            if(wrappedVehicle.getName() != null) {
                prepareUpdateVehicleNameByIdStatement.setString(1, wrappedVehicle.getName());
                prepareUpdateVehicleNameByIdStatement.setInt(2, vehicleId);
                if(prepareUpdateVehicleNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                Output.println("Request completed UPDATE_VEHICLE_NAME_BY_ID");
            }
            if(wrappedVehicle.getCoordinates() != null) {
                prepareUpdateVehicleCoordinatesByIdStatement
                        .setString(1,String.valueOf( wrappedVehicle.getCoordinates().getX()));
                prepareUpdateVehicleCoordinatesByIdStatement
                        .setString(2, String.valueOf(wrappedVehicle.getCoordinates().getY()));
                prepareUpdateVehicleCoordinatesByIdStatement
                        .setInt(3, vehicleId);
                if(prepareUpdateVehicleCoordinatesByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Output.println("Request completed UPDATE_COORDINATES_BY_VEHICLE_ID");
            }
            if(wrappedVehicle.getEnginePower() != null) {
                prepareUpdateVehicleEnginePowerByIdStatement
                        .setString(1,String.valueOf( wrappedVehicle.getEnginePower()));
                prepareUpdateVehicleEnginePowerByIdStatement
                        .setInt(2, vehicleId);
                if(prepareUpdateVehicleEnginePowerByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Output.println("Request completed UPDATE_VEHICLE_ENGINE_POWER_BY_ID");
            }

            if(wrappedVehicle.getCapacity() != null) {
                prepareUpdateVehicleCapacityByIdStatement.setString(1,String.valueOf( wrappedVehicle.getCapacity()));
                prepareUpdateVehicleCapacityByIdStatement.setInt(2, vehicleId);
                if(prepareUpdateVehicleCapacityByIdStatement.executeUpdate() == 0) throw new SQLException();
                Output.println("Request completed UPDATE_VEHICLE_CAPACITY_BY_ID");
            }
            if(wrappedVehicle.getFuelConsumption() != -1) {
                prepareUpdateVehicleConsumptionByIdStatement.setString(1, String.valueOf(wrappedVehicle.getFuelConsumption()));
                prepareUpdateVehicleConsumptionByIdStatement.setInt(2, vehicleId);
                if(prepareUpdateVehicleCapacityByIdStatement.executeUpdate() == 0) throw new SQLException();
                Output.println("Request completed UPDATE_VEHICLE_FUEL_CONSUMPTION_BY_ID");
            }
            if(wrappedVehicle.getFuelType() != null) {
                prepareUpdateVehicleFuelTypeByIdStatement.setString(
                        1,
                        wrappedVehicle.getFuelType().toString());
                prepareUpdateVehicleFuelTypeByIdStatement.setInt(2, vehicleId);
                if(prepareUpdateVehicleFuelTypeByIdStatement.executeUpdate() == 0) throw new SQLException();
                Output.println("Request completed UPDATE_VEHICLE_FUEL_TYPE_BY_ID");
            }

            databaseHandle.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Output.printError("An error occurred while executing a group of requests to update an object!");
            databaseHandle.rollback();
            throw new DatabaseHandlingException();
        } finally{
            databaseHandle.closePreparedStatement(prepareUpdateVehicleNameByIdStatement);
            databaseHandle.closePreparedStatement(prepareUpdateVehicleCoordinatesByIdStatement);
            databaseHandle.closePreparedStatement(prepareUpdateVehicleEnginePowerByIdStatement);
            databaseHandle.closePreparedStatement(prepareUpdateVehicleCapacityByIdStatement);
            databaseHandle.closePreparedStatement(prepareUpdateVehicleConsumptionByIdStatement);
            databaseHandle.closePreparedStatement(prepareUpdateVehicleFuelTypeByIdStatement);
            databaseHandle.setNormalMode();
        }
    }

    public void deleteVehicleById(int vehicleId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteVehicleByIdStatement = null;
        try{
            preparedDeleteVehicleByIdStatement = databaseHandle
                    .getPreparedStatement(DELETE_VEHICLE_BY_ID, false);
            preparedDeleteVehicleByIdStatement.setInt(1, vehicleId);
            if(preparedDeleteVehicleByIdStatement.executeUpdate() == 0) Output.println(3);
            deleteCoordinatesById(vehicleId);
            Output.println("Request completed DELETE_VEHICLE_BY_ID");
        } catch (SQLException e) {
            Output.printError("An error occurred while executing a group of requests to delete an object!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandle.closePreparedStatement(preparedDeleteVehicleByIdStatement);
        }
    }

    public void deleteCoordinatesById(int vehicleId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteCoordinatesByIdStatement = null;
        try{
            preparedDeleteCoordinatesByIdStatement = databaseHandle
                    .getPreparedStatement(DELETE_COORDINATES_BY_ID, false);
            preparedDeleteCoordinatesByIdStatement.setInt(1, vehicleId);
            if(preparedDeleteCoordinatesByIdStatement.executeUpdate() == 0) Output.println("Error with deleting coordinates of vehicle!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseHandle.closePreparedStatement(preparedDeleteCoordinatesByIdStatement);
        }
    }

    public boolean checkVehicleUserId(int vehicleId, User user) throws DatabaseHandlingException{
        PreparedStatement preparedSelectVehicleByIdAndUserIdStatement = null;
        try{
            preparedSelectVehicleByIdAndUserIdStatement = databaseHandle
                    .getPreparedStatement(SELECT_VEHICLE_BY_ID_AND_USER_ID, false);
            preparedSelectVehicleByIdAndUserIdStatement
                    .setInt(1, vehicleId);
            preparedSelectVehicleByIdAndUserIdStatement
                    .setInt(2, databaseUserManager.getUserIdByUserName(user));
            ResultSet resultSet = preparedSelectVehicleByIdAndUserIdStatement.executeQuery();
            Output.println("Request completed SELECT_VEHICLE_BY_ID_AND_USER_ID");
            return resultSet.next();
        }catch (SQLException e) {
            Output.printError("An error occurred while requesting SELECT_VEHICLE_BY_ID_AND_USER_ID");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandle.closePreparedStatement(preparedSelectVehicleByIdAndUserIdStatement);
        }
    }

    public  void clearCollection() throws DatabaseHandlingException {
        ArrayDeque<Vehicle> res = getCollection();
        for(Vehicle vehicle : res) {
            deleteVehicleById(vehicle.getId());
        }
    }

//    public int getUserId(User user) {
//        PreparedStatement preparedGetIdUserStatement;
//        try{
//            preparedGetIdUserStatement = databaseHandle
//                    .getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
//            preparedGetIdUserStatement.setString(1, user.getUsername());
//            preparedGetIdUserStatement.setString(2, user.getPassword());
//            ResultSet resultSet = preparedGetIdUserStatement.executeQuery();
//            if(resultSet.next()) {
//                return resultSet.getInt(DatabaseHandle.USER_TABLE_ID_COLUMN);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }


}

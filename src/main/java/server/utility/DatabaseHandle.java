package server.utility;

import java.sql.*;

public class DatabaseHandle {
    public static final String VEHICLE_TABLE = "vehicle";
    public static final String USER_TABLE = "users";
    public static final String COORDINATES_TABLE = "coordinates";
    //VEHICLE_TABLE column detail
    public static final String VEHICLE_TABLE_ID_COLUMN = "id";
    public static final String VEHICLE_TABLE_NAME_COLUMN = "name";
    public static final String VEHICLE_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String VEHICLE_TABLE_ENGINE_POWER_COLUMN = "engine_power";
    public static final String VEHICLE_TABLE_CAPACITY_POWER_COLUMN = "capacity";
    public static final String VEHICLE_TABLE_FUEL_CONSUMPTION_COLUMN = "fuel_consumption";
    public static final String VEHICLE_TABLE_FUEL_TYPE_COLUMN = "fuel_type";
    public static final String VEHICLE_TABLE_USER_ID_COLUMN = "user_id";
    //USER_TABLE column detail
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    //COORDINATES_TABLE column detail
    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_VEHICLE_ID_COLUMN = "vehicle_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DatabaseHandle(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connectToDatabase();
    }
    private void connectToDatabase() {
        try{
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            Output.println("Database connection established");
        } catch(SQLException e) {
            Output.printError("An error occurred while connecting to the database!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Output.printError("Database management driver not found");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException{
        PreparedStatement preparedStatement;
        try{
            if(connection == null ) throw new SQLException();
            int autoGenerateKeys = generateKeys? Statement.RETURN_GENERATED_KEYS: Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGenerateKeys);

            return preparedStatement;
        } catch (SQLException e) {
            if(connection == null) Output.printError("Database connection not established!");
            throw new SQLException(e);
        }
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if(sqlStatement == null) return ;
        try{
            sqlStatement.close();
        } catch (SQLException e) {
            Output.printError("Cannot close database (>_>)");
        }
    }

    public  void closeConnection() {
        if(connection == null) return ;
        try{
            connection.close();
            Output.println("Connection finished!");
        } catch (SQLException e) {
            Output.printError("An error occurred while closing connection");
        }
    }

    public void setCommitMode() {
        try{
            if(connection == null ) throw new SQLException();
            connection.setAutoCommit(false);
        }catch (SQLException e) {
            Output.printError("An error occurred while set committing mode");
        }
    }

    public void setNormalMode() {
        try{
            if(connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch(SQLException e) {
            Output.printError("An error occurred while setting the database transaction mode!");
        }
    }

    public void commit() {
        try{
            if(connection == null) throw new SQLException();
            connection.commit();
        }catch (SQLException e) {
            Output.printError("An error occurred while committing!");
        }
    }

    public void rollback() {
        try{
            if(connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException e) {
            Output.printError("An error occurred while rolling back");
        }
    }

    public void setSavePoint() {
        try{
            if(connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException e) {
            Output.printError("An error occurred while setting save point");
        }
    }
}

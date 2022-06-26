package server.utility;

import base.Exception.DatabaseHandlingException;
import base.Interaction.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserManager {

    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseHandle.USER_TABLE +
            " WHERE " + DatabaseHandle.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseHandle.USER_TABLE +
            " WHERE " + DatabaseHandle.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseHandle.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseHandle.USER_TABLE + " (" +
            DatabaseHandle.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseHandle.USER_TABLE_PASSWORD_COLUMN + ") VALUES (?, ?)";
    private DatabaseHandle databaseHandle;

    public DatabaseUserManager(DatabaseHandle databaseHandle) {
        this.databaseHandle = databaseHandle;
    }

    public User getUserById(int userId) throws SQLException{
        User user;
        PreparedStatement preparedSelectUserByIdStatement = null;
        try{
            preparedSelectUserByIdStatement =
                    databaseHandle.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedSelectUserByIdStatement.setInt(1, userId);
            ResultSet resultSet = preparedSelectUserByIdStatement.executeQuery();;
            Output.println("Request completed SELECT_USER_BY_ID");
            if(resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseHandle.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseHandle.USER_TABLE_PASSWORD_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            Output.printError("An error occurred while selected column SELECT_USER_BY_ID");
            throw new SQLException(e);
        } finally {
            databaseHandle.closePreparedStatement(preparedSelectUserByIdStatement);
        }

        return user;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectUsernameAndPassword = null;
        try{
            preparedSelectUsernameAndPassword =
                    databaseHandle.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUsernameAndPassword.setString(1, user.getUsername());
            preparedSelectUsernameAndPassword.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUsernameAndPassword.executeQuery();
            Output.println("Request completed SELECT_USER_BY_USERNAME_AND_PASSWORD");

            return resultSet.next();
        } catch (SQLException e) {
            Output.printError("An error occurred while selecting SELECT_USER_BY_USERNAME_AND_PASSWORD");
            throw new DatabaseHandlingException();
        }  finally{
            databaseHandle.closePreparedStatement(preparedSelectUsernameAndPassword);
        }
    }

    public int getUserIdByUserName(User user) throws DatabaseHandlingException {
        int userId;
        PreparedStatement preparedSelectedUsernameStatement = null;

        try{
            preparedSelectedUsernameStatement =
                    databaseHandle.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectedUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectedUsernameStatement.executeQuery();
            Output.println("Request completed SELECT_USER_BY_USERNAME ");
            if(resultSet.next()) {
                userId = resultSet.getInt(DatabaseHandle.USER_TABLE_ID_COLUMN);
            } else userId = -1;
            return userId;
        } catch (SQLException e) {
            Output.printError("An error occurred while selecting SELECT_USER_BY_USERNAME");
            throw new DatabaseHandlingException();
        } finally {
             databaseHandle.closePreparedStatement(preparedSelectedUsernameStatement);
        }
    }

    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedUserStatement = null;
        try{
            if(getUserIdByUserName(user) != -1) return false;
            preparedUserStatement =
                    databaseHandle.getPreparedStatement(INSERT_USER, false);
            preparedUserStatement.setString(1, user.getUsername());
            preparedUserStatement.setString(2, user.getPassword());
            if(preparedUserStatement.executeUpdate() == 0) throw new SQLException();
            Output.println("Request completed INSERT_USER");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Output.printError("An error occurred while selecting INSERT_USER");
            throw new DatabaseHandlingException();
        } finally{
            databaseHandle.closePreparedStatement(preparedUserStatement);
        }
    }
 }


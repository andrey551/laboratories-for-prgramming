package server.commands;

import base.Exception.DatabaseHandlingException;
import base.Exception.UserIsNotFoundException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.DatabaseUserManager;
import server.utility.ResponseOutput;

public class LoginCommand extends AbstractCommand {
    DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("Login", "<account>", "Login user to server");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty()|| obj != null) throw new WrongAmountOfElementsException();
            if(databaseUserManager.checkUserByUsernameAndPassword(user)) ResponseOutput.appendln("Account " +
                    user.getUsername() + " is authorized");
            else throw new UserIsNotFoundException();

            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage : Login command");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendError("An error occurred while accessing the database");
        } catch (UserIsNotFoundException e) {
           ResponseOutput.appendError("Username or password is incorrect!");
        }

        return false;
    }
}

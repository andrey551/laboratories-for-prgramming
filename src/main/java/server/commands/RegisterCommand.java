package server.commands;

import base.Exception.DatabaseHandlingException;
import base.Exception.UserAlreadyExistException;
import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.DatabaseUserManager;
import server.utility.ResponseOutput;

public class RegisterCommand extends  AbstractCommand{

    private DatabaseUserManager databaseUserManager;
    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("Register", "", "create new account");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty() || obj != null) throw new WrongAmountOfElementsException();
            if(databaseUserManager.insertUser(user)) ResponseOutput.appendln("User" +
                    user.getUsername() + "is registered");
            else throw new UserAlreadyExistException();

            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage register command");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendln("An error occurred while registering");
        } catch (UserAlreadyExistException e) {
            ResponseOutput.appendln("username has registered!");
        }
        return false;
    }
}

package server.commands;

import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;

public class ClientExitCommand extends  AbstractCommand{
    public ClientExitCommand() {
        super("exit_client", "",  "User quit app");
    }

    @Override
    public Boolean execute(String stringArgument, Object obj, User user) {
        try{
            if(!stringArgument.isEmpty() || obj != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage" + getName());
        }

        return false;
    }
}
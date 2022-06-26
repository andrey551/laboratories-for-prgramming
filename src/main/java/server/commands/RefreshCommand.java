package server.commands;

import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;

public class RefreshCommand extends AbstractCommand{

    public RefreshCommand() {
        super("refresh", "", "internal command");
    }

    @Override
    public Boolean execute(String stringArgument, Object obj, User user) {
        try{
            if(!stringArgument.isEmpty() || obj != null) throw new WrongAmountOfElementsException();
            return true;
        } catch(WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendln(getName() + getUsage() + " ");
        }
        return false;
    }

}

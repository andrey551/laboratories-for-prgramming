package server.commands;

import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;

/**
 * Command 'help'. Display help on available commands
 */
public class HelpCommand extends AbstractCommand{
    public HelpCommand() {
        super("help","", "Display help on available commands");
    }
/**
 * Executes the command.
 * @return Command exit status.
 */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty()|| obj != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException e){
            ResponseOutput.appendln("Usage: " + getName() + " ");
        }
        return false;
    }
}

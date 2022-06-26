package server.commands;

import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;

/**
 * Command 'exit'. Finish program( without save data to file)
 */
public class ExitCommand  extends AbstractCommand{

    public ExitCommand() {
        super("Exit server"," ","Finish program" );
    }
    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try{
            if(!argument.isEmpty()|| obj != null) throw new WrongAmountOfElementsException();
            ResponseOutput.appendln("Server has finished!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Usage: " + getName() + " ");
        }

        return false;
    }
    
}

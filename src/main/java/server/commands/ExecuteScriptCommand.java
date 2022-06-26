package server.commands;

import base.Exception.WrongAmountOfElementsException;
import base.Interaction.User;
import server.utility.ResponseOutput;

import java.util.Stack;

/**
 * Command 'execute_script'. Execute script from specified file
 */
public class ExecuteScriptCommand extends AbstractCommand{
    private static Stack<String> callStack = new Stack<>();

    public static void clearStack(){
        callStack.clear();
    }

    public ExecuteScriptCommand() {
        super("execute_script","<file_name>", "Execute script from specified file");
    }
    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public Boolean execute(String argument, Object obj, User user) {
        try {
            if (argument.isEmpty()|| obj != null) throw new WrongAmountOfElementsException();
            ResponseOutput.appendln("Executing script " + argument + "'...");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Usage: " + getName() +" " + getUsage() + "'");
        }
        return false;
    }

}

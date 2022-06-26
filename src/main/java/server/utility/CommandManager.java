package server.utility;

import base.Interaction.User;
import base.Interaction.WrappedVehicle;
import server.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Operates the commands.
 */
public class CommandManager {
    private final int COMMAND_HISTORY_SIZE = 8;

    private String[] commandHistory = new String[COMMAND_HISTORY_SIZE];
    private List<Command> commands = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command addCommand;
    private Command updateCommand;
    private Command removeByIdCommand;
    private Command clearCommand;
    private Command executeCommand;
    private Command ClientExitCommand;
    private Command removeFirstCommand;
    private Command addIfMaxCommand;
    private Command removeGreaterCommand;
    private Command groupCountingByCapacityCommand;
    private Command countLessThanFuelTypeCommand;
    private Command filterLessThanFuelConsumptionCommand;
    private Command loginCommand;
    private Command registerCommand;
    private Command refreshCommand;

    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public CommandManager(Command helpCommand,Command infoCommand, Command showCommand,Command addCommand,
                        Command updateCommand, Command removeByIdCommand, Command clearCommand,
                        Command executeCommand, Command ClientExitCommand, Command removeFirstCommand, Command addIfMaxCommand,
                        Command removeGreaterCommand, Command groupCountingByCapacityCommand,
                        Command countLessThanFuelTypeCommand, Command filterLessThanFuelConsumptionCommand,
                          Command loginCommand, Command registerCommand, Command refreshCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.addCommand = addCommand;
        this.updateCommand = updateCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.clearCommand = clearCommand;
        this.executeCommand = executeCommand;
        this.ClientExitCommand = ClientExitCommand;
        this.removeFirstCommand = removeFirstCommand;
        this.addIfMaxCommand = addIfMaxCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.groupCountingByCapacityCommand = groupCountingByCapacityCommand;
        this.countLessThanFuelTypeCommand = countLessThanFuelTypeCommand;
        this.filterLessThanFuelConsumptionCommand = filterLessThanFuelConsumptionCommand;
        this.loginCommand = loginCommand;
        this.registerCommand = registerCommand;
        this.refreshCommand = refreshCommand;

        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(addCommand);
        commands.add(updateCommand);
        commands.add(removeByIdCommand);
        commands.add(clearCommand);
        commands.add(executeCommand);
        commands.add(ClientExitCommand);
        commands.add(removeFirstCommand);
        commands.add(addIfMaxCommand);
        commands.add(removeGreaterCommand);
        commands.add(groupCountingByCapacityCommand);
        commands.add(countLessThanFuelTypeCommand);
        commands.add(filterLessThanFuelConsumptionCommand);
        commands.add(loginCommand);
        commands.add(registerCommand);
        commands.add(refreshCommand);
    }

    public List<Command> getCommands() {
        return commands;
    }
    /**
     * Prints that command is not found.
     * @param command Command, which is not found.
     * @return Command exit status.
     */
    public boolean noSuchCommand(String command) {
        ResponseOutput.appendln("Command '" + command + "' is not exist. Type help for help");
        return false;
    }
    /**
     * Prints info about the all commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean help(String argument, User user) {
        if (helpCommand.execute(argument, null, user)) {
            for (Command command : commands) {
                ResponseOutput.appendTable(command.getName(), command.getDescription());
            }
            return true;
        } else return false;
    }
    /**
     * Prints info about the all commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean info(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return infoCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Prints info about the all commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean show(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return showCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Prints info about the all commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean add(String argument, WrappedVehicle vehicle, User user) {
        collectionLocker.readLock().lock();
        try {
            return addCommand.execute(argument, vehicle, user);
        }finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean update(String argument, WrappedVehicle vehicle, User user) {
        collectionLocker.readLock().lock();
        try {
            return updateCommand.execute(argument, vehicle, user);
        }finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeById(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return removeByIdCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean clear(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return clearCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean exit(String argument, User user) {
        return ClientExitCommand.execute(argument, null, user);
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean executeScript(String argument, User user) {
        return executeCommand.execute(argument, null, user);
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean addIfMax(String argument, WrappedVehicle vehicle, User user) {
        collectionLocker.readLock().lock();
        try {
            return addIfMaxCommand.execute(argument, vehicle, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeGreater(String argument, WrappedVehicle vehicle, User user) {
        collectionLocker.readLock().lock();
        try {
            return removeGreaterCommand.execute(argument, vehicle, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeFirst(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return removeFirstCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean groupCountingByCapacity(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return groupCountingByCapacityCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean countLessThanFuelType(String argument, User user ) {
        collectionLocker.readLock().lock();
        try {
            return countLessThanFuelTypeCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }
    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean filterLessThanFuelConsumption(String argument, User user) {
        collectionLocker.readLock().lock();
        try {
            return filterLessThanFuelConsumptionCommand.execute(argument, null, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public boolean login(String argument, User user) {
        return loginCommand.execute(argument, null, user);
    }

    public boolean register(String argument, User user) {
        return registerCommand.execute(argument,null, user);
    }

    public boolean refresh(String argument, User user) { return refreshCommand.execute(argument, null, user);}

}

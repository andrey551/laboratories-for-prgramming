package server.commands;

import base.Interaction.User;

/**
 * Interface Command
 */
public interface Command {
    String getDescription();
    String getUsage();
    String getName();
    Boolean execute(String argument, Object obj, User user);
}

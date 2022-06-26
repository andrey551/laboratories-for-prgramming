package base.Interaction;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private String commandArgument;
    private Serializable objArgument;
    private User user;

    public Request(String commandName, String commandArgument, Serializable objArgument, User user) {
        this.commandName = commandName;
        this.commandArgument = commandArgument;
        this.objArgument = objArgument;
        this.user = user;
    }

    public Request(String commandName, String commandArgument, User user) {
        this(commandName, commandArgument, null,user);
    }

    public Request(User user) {
        this("", "", user);
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandArgument(){
        return this.commandArgument;
    }

    public Object getObjectArgument() {
        return this.objArgument;
    }

    public boolean isEmpty() {
        return commandName.isEmpty() && commandArgument.isEmpty() && objArgument == null;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", commandArgument='" + commandArgument + '\'' +
                ", objArgument=" + objArgument +
                "User=" +user +
                '}';
    }

    @Override
    public int hashCode() {
        return this.commandName.hashCode() + this.commandArgument.hashCode() + objArgument.hashCode() + user.hashCode();
    }


}


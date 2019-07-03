package com.github.riverxik.meowbot.commands;

/**
 * Represents all possible commands in the chat
 * @author RiVeRx
 * @version 1.0
 * */
public class Command {

    /**
     * Name of the command
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns all possible parameters for command
     * @return Object massive with value of parameters
     */
    public Object[] getParameters() {
        return parameters;
    }

    private String name = null;
    private Object[] parameters = null;

    /**
     * Command constructor
     * @param name String name of a command
     * @param parameters Object massive of parameters
     */
    public Command(String name, Object[] parameters) {
        this.name = name;
        this.parameters = parameters;
    }
}

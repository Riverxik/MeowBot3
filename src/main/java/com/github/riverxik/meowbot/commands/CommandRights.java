package com.github.riverxik.meowbot.commands;

public enum CommandRights {
    EVERYONE (0),
    VIP_SUB (1),
    MODERATOR (2),
    OWNER (3);

    private final int index;

    CommandRights(int index) { this.index = index; }

    public int index() { return this.index; }
}

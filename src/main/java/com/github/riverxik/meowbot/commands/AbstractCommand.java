package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all possible commands in the chat
 * @author RiVeRx
 * @version 1.0
 * */
public abstract class AbstractCommand implements ICommand{
    public static Logger log = LoggerFactory.getLogger(AbstractCommand.class);

    public boolean hasRight(String channel, String sender, CommandRights level) {
        return level.index() <= Configuration.getChannelByName(channel).getChannelUserByName(sender).getRightLevel().index();
    }
}

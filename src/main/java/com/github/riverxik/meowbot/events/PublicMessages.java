package com.github.riverxik.meowbot.events;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.fsa.Lexer;
import com.github.riverxik.meowbot.commands.fsa.Parser;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.modules.alias.AliasManagerUtils;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Writes chat messages from channels.
 * @author RiVeRx
 * @version 1.0
 */
public class PublicMessages {

    private static final Logger log = LoggerFactory.getLogger(PublicMessages.class);
    /**
     * Handles new channel messages.
     * @param eventManager - event manager from TwitchClient {@link TwitchBotHelper#twitchClient}
     */
    public PublicMessages(EventManager eventManager) {
        eventManager.onEvent(ChannelMessageEvent.class).subscribe(this::onChannelMessage);
    }

    private void onChannelMessage(ChannelMessageEvent event) {
        String channel = event.getChannel().getName();
        String sender = event.getUser().getName();
        String message = event.getMessage();
        TwitchChat chat = event.getTwitchChat();

        log.info(String.format("[%s][%s] - [%s]", channel, sender, message));

        // Active increasing currency
        ConfigurationUtils.getChannelByName(channel).getChannelUserByName(sender).increaseMessages();

        if(message.startsWith("!")) {
            Object[] commandParts = parseCommand(message);
            String baseCommand = String.valueOf(commandParts[0]).toLowerCase();
            Object[] args = Arrays.copyOfRange(commandParts, 1, commandParts.length);

            //Проверка на алиас
            baseCommand = AliasManagerUtils.findCommandByAlias(channel, baseCommand);
            // Выполнение
            processCommand(channel, sender, baseCommand, args, chat);
        }

        if(event.getMessage().equals("meow"))
            event.getTwitchChat().sendMessage(event.getChannel().getName(), event.getUser().getName() + " meow <3");
    }

    private void processCommand(String channel, String sender, String baseCommand, Object[] args, TwitchChat chat) {
        if (ConfigurationUtils.commandRegistry.containsKey(baseCommand)) {
            ConfigurationUtils.commandRegistry.get(baseCommand).execute(channel, sender, args, chat);
        } else {
            log.info("Unknown command: " + baseCommand);
        }
    }

    private static Object[] parseCommand(String message) {
        try {
            Lexer lexer = new Lexer(message);
            lexer.tokenize(false);
            Parser parser = new Parser(lexer.getTokenList());
            parser.start(false);

            int sizeOfStackValues = parser.stackValues.size();

            Object[] params = new Object[sizeOfStackValues];
            for (int i = 0; i < sizeOfStackValues; i++) {
                params[i] = parser.stackValues.get(i);
            }
            return params;
        } catch (RuntimeException e) {
            return new Object[] {"error", e.getMessage()};
        }
    }
}

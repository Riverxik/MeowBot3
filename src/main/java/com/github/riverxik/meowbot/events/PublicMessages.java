package com.github.riverxik.meowbot.events;

import com.github.riverxik.meowbot.modules.PublicMessageManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes chat messages from channels.
 * @author RiVeRx
 * @version 1.0
 */
public class PublicMessages {

    private static final Logger log = LoggerFactory.getLogger(PublicMessages.class);
    /**
     * Handles new channel messages.
     * @param eventManager - event manager from TwitchClient {@link TwitchBot#twitchClient}
     */
    public PublicMessages(EventManager eventManager) {
        eventManager.onEvent(ChannelMessageEvent.class).subscribe(event -> onChannelMessage(event));
    }

    private void onChannelMessage(ChannelMessageEvent event) {
        String channel = event.getChannel().getName();
        String sender = event.getUser().getName();
        String message = event.getMessage();

        log.info(String.format("[%s][%s] - [%s]", channel, sender, message));

        if(message.startsWith("!")) {
            String botAnswer = PublicMessageManager.processNewMessage(channel, sender, message);
            event.getTwitchChat().sendMessage(channel, sender + " " + botAnswer);
            log.info(String.format("[%s][%s] - [%s]", channel, "Bot", botAnswer));
        }

        if(event.getMessage().equals("meow"))
            event.getTwitchChat().sendMessage(event.getChannel().getName(), event.getUser().getName() + " meow <3");
    }
}

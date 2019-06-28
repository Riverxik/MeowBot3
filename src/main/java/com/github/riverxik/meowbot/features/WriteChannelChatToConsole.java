package com.github.riverxik.meowbot.features;

import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

/**
 * Writes chat messages from channels.
 * @author RiVeRx
 * @version 1.0
 */
public class WriteChannelChatToConsole {
    /**
     * Handles new channel messages.
     * @param eventManager - event manager from TwitchClient {@link TwitchBot#twitchClient}
     */
    public WriteChannelChatToConsole(EventManager eventManager) {
        eventManager.onEvent(ChannelMessageEvent.class).subscribe(event -> onChannelMessage(event));
    }

    private void onChannelMessage(ChannelMessageEvent event) {
        System.out.printf(
                "Channel [%s] - User[%s] - Message [%s]%n",
                event.getChannel().getName(),
                event.getUser().getName(),
                event.getMessage()
        );
        if(event.getMessage().equals("meow"))
            event.getTwitchChat().sendMessage(event.getChannel().getName(), event.getUser().getName() + " meow <3");
    }
}

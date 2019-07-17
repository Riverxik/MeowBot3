package com.github.riverxik.meowbot.events;

import com.github.riverxik.meowbot.modules.PrivateMessageManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;

/**
 * Works with private messages
 * @author RiVeRx
 * @version 1.0
 */
public class PrivateMessages {

    /**
     * Handles the private message event
     * @param eventManager - event manager from TwitchClient {@link TwitchBot#twitchClient}
     */
    public PrivateMessages(EventManager eventManager) {
        eventManager.onEvent(PrivateMessageEvent.class).subscribe(event -> onPrivateMessage(event));
    }

    private void onPrivateMessage(PrivateMessageEvent event) {
        String sender = event.getUser().getName();
        String message = event.getMessage();

        String botAnswer = PrivateMessageManager.processNewMessage(sender, message);
        TwitchBot.sendPmMessage(sender, botAnswer);
    }
}

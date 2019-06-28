package com.github.riverxik.meowbot.features;

import com.github.riverxik.meowbot.modules.PrivateMessageManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Works with private messages
 * @author RiVeRx
 * @version 1.0
 */
public class CheckPrivateMessages {

    private static final Logger log = LoggerFactory.getLogger(CheckPrivateMessages.class);

    /**
     * Handles the private message event
     * @param eventManager - event manager from TwitchClient {@link TwitchBot#twitchClient}
     */
    public CheckPrivateMessages(EventManager eventManager) {
        eventManager.onEvent(PrivateMessageEvent.class).subscribe(event -> OnPrivateMessage(event));
    }

    private void OnPrivateMessage(PrivateMessageEvent event) {
        String sender = event.getUser().getName();
        String message = event.getMessage();

        String botAnswer = PrivateMessageManager.processNewMessage(sender, message);
        TwitchBot.sendPmMessage(sender, botAnswer);
    }
}

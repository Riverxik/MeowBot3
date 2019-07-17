package com.github.riverxik.meowbot.events;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.roomstate.SubscribersOnlyEvent;

public class SubscribersOnly {

    public SubscribersOnly(EventManager eventManager) {
        eventManager.onEvent(SubscribersOnlyEvent.class).subscribe(this::onSubscribersOnly);
    }

    private void onSubscribersOnly(SubscribersOnlyEvent event) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), "Unsub's are resting now DxCat");
    }
}

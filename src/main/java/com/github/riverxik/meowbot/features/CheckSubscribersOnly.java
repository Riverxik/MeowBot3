package com.github.riverxik.meowbot.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.roomstate.SubscribersOnlyEvent;

public class CheckSubscribersOnly {

    public CheckSubscribersOnly(EventManager eventManager) {
        eventManager.onEvent(SubscribersOnlyEvent.class).subscribe(event -> OnSubscribersOnly(event));
    }

    private void OnSubscribersOnly(SubscribersOnlyEvent event) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), "Unsub's are resting now DxCat");
    }
}

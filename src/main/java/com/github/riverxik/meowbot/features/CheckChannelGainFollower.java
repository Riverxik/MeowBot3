package com.github.riverxik.meowbot.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.Configuration;
import com.github.twitch4j.chat.events.channel.FollowEvent;

public class CheckChannelGainFollower {

    public CheckChannelGainFollower(EventManager eventManager) {
        eventManager.onEvent(FollowEvent.class).subscribe(event -> OnFollowEvent(event));
    }

    private void OnFollowEvent(FollowEvent event) {
        String channelName = event.getChannel().getName();
        String userName = event.getUser().getName();
        if(Configuration.isStreamFollower())
            event.getTwitchChat().sendMessage(channelName, String.format("Welcome, %s! Thank you for follow <3", userName));
        else
            System.out.println(String.format("Welcome, %s! Thank you for follow <3", userName));
    }
}

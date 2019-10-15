package com.github.riverxik.meowbot.events;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.twitch4j.chat.events.channel.FollowEvent;

public class ChannelGainFollower {

    public ChannelGainFollower(EventManager eventManager) {
        eventManager.onEvent(FollowEvent.class).subscribe(this::onFollowEvent);
    }

    private void onFollowEvent(FollowEvent event) {
        String channelName = event.getChannel().getName();
        String userName = event.getUser().getName();
        if(ConfigurationUtils.isStreamFollower())
            event.getTwitchChat().sendMessage(channelName, String.format("Welcome, %s! Thank you for follow <3", userName));
        else
            System.out.println(String.format("Welcome, %s! Thank you for follow <3", userName));
    }
}

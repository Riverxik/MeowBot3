package com.github.riverxik.meowbot.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;

public class CheckChannelGoesOffline {

    public CheckChannelGoesOffline(EventManager eventManager) {
        eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(event -> OnChannelOffline(event));
    }

    private void OnChannelOffline(ChannelGoOfflineEvent event) {
        String channelName = event.getChannel().getName();
        if(Configuration.isStreamOfflineEnable())
            TwitchBot.twitchClient.getChat().sendMessage(channelName, String.format("[%s] Goes offline", channelName));
        else
            System.out.println(String.format("[%s] Goes offline", channelName));
    }
}

package com.github.riverxik.meowbot.events;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;

public class ChannelGoesOffline {

    public ChannelGoesOffline(EventManager eventManager) {
        eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(this::onChannelOffline);
    }

    private void onChannelOffline(ChannelGoOfflineEvent event) {
        String channelName = event.getChannel().getName();
        if(ConfigurationUtils.isStreamOfflineEnable())
            TwitchBotHelper.getTwitchClient().getChat().sendMessage(channelName, String.format("[%s] Goes offline", channelName));
        else
            System.out.println(String.format("[%s] Goes offline", channelName));
    }
}

package com.github.riverxik.meowbot.events;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelLiveEvents {

    private static final Logger log = LoggerFactory.getLogger(ChannelLiveEvents.class);

    public ChannelLiveEvents(EventManager eventManager) {
        eventManager.onEvent(ChannelGoLiveEvent.class).subscribe(this::onChannelGoesLive);
        eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(this::onChannelGoesOffline);
    }

    private void onChannelGoesLive(ChannelGoLiveEvent event) {
        String channelName = event.getChannel().getName();
        String title = event.getTitle();
        String game = event.getGameId();
        if(ConfigurationUtils.isStreamLiveEnable()) {
            TwitchBotHelper.sendMessageToChat(channelName, String.format("[%s] starts: %s on game: %s", channelName, title, game));
        } else {
            System.out.println(String.format("[%s] starts: %s on game: %s", channelName, title, game));
        }
        log.info(String.format("[%s] starts: %s on game: %s", channelName, title, game));
    }

    private void onChannelGoesOffline(ChannelGoOfflineEvent event) {
        String channelName = event.getChannel().getName();
        if (ConfigurationUtils.isStreamOfflineEnable()) {
            TwitchBotHelper.sendMessageToChat(channelName,
                    String.format("Seems like [%s] went offline. See you KonCha", channelName));
        } else {
            System.out.println(String.format("Seems like [%s] went offline. See you KonCha", channelName));
        }
        log.info(String.format("Seems like [%s] went offline. See you KonCha", channelName));
    }
}

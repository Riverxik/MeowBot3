package com.github.riverxik.meowbot.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckChannelGoesLive {

    private static final Logger log = LoggerFactory.getLogger(CheckChannelGoesLive.class);

    public CheckChannelGoesLive(EventManager eventManager) {
        eventManager.onEvent(ChannelGoLiveEvent.class).subscribe(event -> OnChannelGoLive(event));
    }

    private void OnChannelGoLive(ChannelGoLiveEvent event) {
        String channelName = event.getChannel().getName();
        String title = event.getTitle();
        long game = event.getGameId();
        if(Configuration.isStreamLiveEnable())
            TwitchBot.sendMessageToChat(channelName, String.format("[%s] starts: %s on game: %s", channelName, title, game));
        else
            System.out.println(String.format("[%s] starts: %s on game: %s", channelName, title, game));
    }
}

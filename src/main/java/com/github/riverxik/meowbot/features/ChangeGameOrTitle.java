package com.github.riverxik.meowbot.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.common.events.channel.ChannelChangeGameEvent;
import com.github.twitch4j.common.events.channel.ChannelChangeTitleEvent;

public class ChangeGameOrTitle {

    public ChangeGameOrTitle(EventManager eventManager) {
        eventManager.onEvent(ChannelChangeGameEvent.class).subscribe(event -> OnChangeGame(event));
        eventManager.onEvent(ChannelChangeTitleEvent.class).subscribe(event -> OnChangeTitle(event));
    }

    private void OnChangeGame(ChannelChangeGameEvent event) {
        String channelName = event.getChannel().getName();
        long game = event.getGameId();
        TwitchBot.sendMessageToChat(channelName, String.format("[%s] now playing: %s", channelName, game));
    }

    private void OnChangeTitle(ChannelChangeTitleEvent event) {
        String channelName = event.getChannel().getName();
        String title = event.getTitle();
        TwitchBot.sendMessageToChat(channelName, String.format("[%s] change title to: %s", channelName, title));
    }
}

package com.github.riverxik.meowbot.events;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.common.events.channel.ChannelChangeGameEvent;
import com.github.twitch4j.common.events.channel.ChannelChangeTitleEvent;
import com.github.twitch4j.helix.domain.GameList;

import java.util.Arrays;

public class ChangeGameOrTitle {

    public ChangeGameOrTitle(EventManager eventManager) {
        eventManager.onEvent(ChannelChangeGameEvent.class).subscribe(event -> OnChangeGame(event));
        eventManager.onEvent(ChannelChangeTitleEvent.class).subscribe(event -> OnChangeTitle(event));
    }

    private void OnChangeGame(ChannelChangeGameEvent event) {
        String channelName = event.getChannel().getName();
        String game = getGameNameById(String.valueOf(event.getGameId()));
        TwitchBot.sendMessageToChat(channelName, String.format("[%s] now playing: %s", channelName, game));
    }

    private void OnChangeTitle(ChannelChangeTitleEvent event) {
        String channelName = event.getChannel().getName();
        String title = event.getTitle();
        TwitchBot.sendMessageToChat(channelName, String.format("[%s] change title to: %s", channelName, title));
    }

    private String getGameNameById(String gameId) {
        GameList gameList = TwitchBot.twitchClient.getHelix().getGames(Arrays.asList(gameId), null).execute();
        return gameList.getGames().get(0).getName();
    }
}

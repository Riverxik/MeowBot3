package com.github.riverxik.meowbot.events;

import com.github.philippheuer.events4j.EventManager;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.twitch4j.common.events.channel.ChannelChangeGameEvent;
import com.github.twitch4j.common.events.channel.ChannelChangeTitleEvent;
import com.github.twitch4j.helix.domain.GameList;

import java.util.Collections;

public class ChangeGameOrTitle {

    public ChangeGameOrTitle(EventManager eventManager) {
        eventManager.onEvent(ChannelChangeGameEvent.class).subscribe(this::onChangeGame);
        eventManager.onEvent(ChannelChangeTitleEvent.class).subscribe(this::onChangeTitle);
    }

    private void onChangeGame(ChannelChangeGameEvent event) {
        String channelName = event.getChannel().getName();
        String game = getGameNameById(String.valueOf(event.getGameId()));
        TwitchBotHelper.sendMessageToChat(channelName, String.format("[%s] now playing: %s", channelName, game));
    }

    private void onChangeTitle(ChannelChangeTitleEvent event) {
        String channelName = event.getChannel().getName();
        String title = event.getTitle();
        TwitchBotHelper.sendMessageToChat(channelName, String.format("[%s] change title to: %s", channelName, title));
    }

    private String getGameNameById(String gameId) {
        GameList gameList = TwitchBotHelper.getTwitchClient().getHelix()
                .getGames(Collections.singletonList(gameId), null).execute();
        return gameList.getGames().get(0).getName();
    }
}

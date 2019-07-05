package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.ChannelDb;
import com.github.riverxik.meowbot.events.*;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents twitch instance of bot.
 * @author RiVeRx
 * @version 1.0
 */
public class TwitchBot {

    private static final Logger log = LoggerFactory.getLogger(TwitchBot.class);
    public static TwitchClient twitchClient;

    /** Constructs twitch bot instance */
    public TwitchBot() {
        log.info("Initializing twitch-service...");
        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();

        OAuth2Credential credential = new OAuth2Credential(
                "twitch",
                Configuration.chatToken
        );

        twitchClient = clientBuilder
                .withClientId(Configuration.clientAppId)
                .withClientSecret(Configuration.clientAppSecret)
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .withEnableTMI(true)
                .build();
    }

    /** Registers all events */
    public void registerFeatures() {
        log.info("Component registration...");
        PublicMessages publicMessages = new PublicMessages(twitchClient.getEventManager());
        PrivateMessages privateMessages = new PrivateMessages(twitchClient.getEventManager());
        SubscribersOnly subscribersOnly = new SubscribersOnly(twitchClient.getEventManager());
        ChangeGameOrTitle changeGameOrTitle = new ChangeGameOrTitle(twitchClient.getEventManager());
        if (Configuration.isStreamLiveEnable()) {
            ChannelGoesLive channelGoesLive = new ChannelGoesLive(twitchClient.getEventManager());
        }
        if (Configuration.isStreamOfflineEnable()) {
            ChannelGoesOffline channelGoesOffline = new ChannelGoesOffline(twitchClient.getEventManager());
        }
        if (Configuration.isStreamFollower()) {
            ChannelGainFollower channelGainFollower = new ChannelGainFollower(twitchClient.getEventManager());
        }
        // TODO: Maybe i should put all this events together to one class.
        // add events
    }

    /** Connects to all twitch channels */
    public void start() {
        log.info("Connecting to channels...");
        for(ChannelDb channel : Configuration.loadingChannels) {
            String channelName = channel.getChannelName();
            twitchClient.getChat().joinChannel(channelName);
            twitchClient.getClientHelper().enableStreamEventListener(channelName);
        }
        log.info("Successfully connected to all the channels!");
    }

    /**
     * Sends message to chat.
     * @param channelName - channel name to send message.
     * @param message - string to send in chat.
     */
    public static void sendMessageToChat(String channelName, String message) {
        twitchClient.getChat().sendMessage(channelName, message);
    }

    /**
     * Sends private message.
     * @param userName - nickname of user to send message.
     * @param message - string to send.
     */
    public static void sendPmMessage(String userName, String message) {
        twitchClient.getChat().sendMessage("murameowbot", String.format("/w %s %s", userName, message));
    }
}

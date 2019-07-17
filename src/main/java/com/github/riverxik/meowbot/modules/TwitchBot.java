package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.events.*;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents twitch instance of bot.
 * @author RiVeRx
 * @version 1.0
 */
public final class TwitchBot {

    private static final Logger log = LoggerFactory.getLogger(TwitchBot.class);
    private static TwitchClient twitchClient = null;

    public static void initialize() {
        log.info("Initializing twitch-service...");
        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();

        OAuth2Credential credential = new OAuth2Credential(
                "twitch",
                Configuration.chatToken
        );

        twitchClient = clientBuilder
                .withClientId(Configuration.clientAppId)
                .withClientSecret(Configuration.clientAppSecret)
                .withEnableKraken(true)
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .withEnableTMI(true)
                .build();
    }

    public static TwitchClient getTwitchClient() { return twitchClient; }

    /** Registers all events */
    public static void registerFeatures() {
        log.info("Component registration...");
        new PublicMessages(twitchClient.getEventManager());
        new SubscribersOnly(twitchClient.getEventManager());
        new ChangeGameOrTitle(twitchClient.getEventManager());
        // TODO: Maybe i should put all this events together to one class.
        // add events
    }

    /** Connects to all twitch channels */
    public static void start() {
        log.info("Connecting to channels...");
        for(Channel channel : Configuration.loadingChannels) {
            String channelName = channel.getName();
            twitchClient.getChat().joinChannel(channelName);
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
}

package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.ChannelDb;
import com.github.riverxik.meowbot.features.CheckPrivateMessages;
import com.github.riverxik.meowbot.features.WriteChannelChatToConsole;
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
    static TwitchClient twitchClient;

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

    /** Registers all features */
    public void registerFeatures() {
        log.info("Component registration...");
        WriteChannelChatToConsole channelChatToConsole = new WriteChannelChatToConsole(twitchClient.getEventManager());
        CheckPrivateMessages checkPrivateMessages = new CheckPrivateMessages(twitchClient.getEventManager());
        // add features
    }

    /** Connects to all twitch channels */
    public void start() {
        log.info("Connecting to channels...");
        for(ChannelDb channel : Configuration.loadingChannels) {
            twitchClient.getChat().joinChannel(channel.getChannelName());
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

package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

import java.util.ArrayList;

public class CustomCommandHandler extends AbstractCommand{

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        /*
            1. Определить текст команды используя имя команды args[0] и имя канала channel.
            2. Проверить валидность аргументов.
            3. Правильно распарсить текст команды используя аргументы.
         */
        if (args.length > 0 && args[0] instanceof String) {
            String cmdName = args[0].toString();
            String cmdText = CustomCommandUtils.getCommandText(channel, cmdName);
            ArrayList<String> subArgs = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {
                subArgs.add(args[i].toString());
            }

            String answer = executeCommandText(channel, sender, cmdText, subArgs);
            chat.sendMessage(channel, answer);
            return true;
        }
        return false;
    }

    private String executeCommandText(String channel, String sender, String cmdText, ArrayList<String> subArgs) {
        String[] split = cmdText.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            switch (split[i]) {
                case "%username": builder.append(sender).append(" "); break;
                case "%streamer": builder.append(channel).append(" "); break;
                case "%randomUser": builder.append(getRandomUser(channel)).append(" "); break;
                case "%randomNumber": builder.append(getRandomNumber()).append(" "); break;
                case "%1": {
                    if (subArgs.size() > 1)
                        builder.append(subArgs.get(1)).append(" ");
                    else
                        builder.append("Kappa").append(" ");
                } break;
                case "%2": {
                    if (subArgs.size() > 2)
                        builder.append(subArgs.get(2)).append(" ");
                    else
                        builder.append("PogChamp").append(" ");
                } break;
                default: builder.append(split[i]).append(" "); break;
            }
        }
        return builder.toString();
    }

    private String getRandomUser(String channel) {
        int maxUsers = ConfigurationUtils.getChannelByName(channel).getUsersCount();
        if (maxUsers == 0) {
            return "Twitch-bot";
        } else {
            int happyNumber = (int) (Math.random() * maxUsers);
            return ConfigurationUtils.getChannelByName(channel).getChannelUserById(happyNumber).getName();
        }
    }

    private String getRandomNumber() {
        return String.valueOf((int) (Math.random() * 100));
    }
}

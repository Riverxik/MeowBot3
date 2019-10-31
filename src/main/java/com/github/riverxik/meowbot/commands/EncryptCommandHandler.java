package com.github.riverxik.meowbot.commands;

import com.github.twitch4j.chat.TwitchChat;

public class EncryptCommandHandler extends AbstractCommand {
    // !encrypt <String>, <numOfIterate>
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 1 && args[0] instanceof String && args[1] instanceof Integer) {
            String text = String.valueOf(args[0]);
            int count = (int) args[1];
            String answer = deEncrypt(text, count);
            chat.sendMessage(channel, String.format("%s, Your encrypted message: %s", sender, answer));
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !encrypt \"Text\" 3", sender));
        }
    }

    private static String deEncrypt(String text, int count) {
        String alphaStr = "абвгдеёжзиклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = alphaStr.length()-1;
        int cnt = count % length;
        char[] array = text.toCharArray();
        StringBuilder result = new StringBuilder();

        for (int j = 0; j < array.length; j++) {
            char tmp = array[j];
            if (Character.isLetterOrDigit(tmp)) {
                int ind = alphaStr.indexOf(tmp);
                ind += cnt;
                if (ind > alphaStr.length()-1)
                    ind = ind%length;
                if (ind < 0){
                    ind += length;
                }
                array[j] = alphaStr.charAt(ind);
            }
            result.append(array[j]);
        }
        return result.toString();
    }
}

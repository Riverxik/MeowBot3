package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.commands.fsa.Lexer;
import com.github.riverxik.meowbot.commands.fsa.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicMessageManager {
    private static final Logger log = LoggerFactory.getLogger(PrivateMessageManager.class);

    public static String processNewMessage(String channel, String sender, String message) {
        /*
         * Принимает сообщение, выбирает куда дальше переслать.
         * Сообщение может быть получено от админа, либо от пользователей.
         * Если сообщенние от админа, то обрабатываем все команды.
         * Если сообщение от пользователя, то обрабатываем только команды для пользователей.
         *
         * Получаем отправителя.
         * Получаем администратора.
         * Если сообщение от админа передаём в обработчик для админа.
         * иначе отдаем в обработчик для пользователей.
         * */
        Command userCommand = parseCommand(message);
        if(sender.equals(Configuration.admin))
            return processAdminCommand(channel, sender, userCommand);
        else
            return processUserCommand(channel, sender, userCommand);
    }

    private static String processAdminCommand(String channel, String sender, Command command) {
        switch (command.getName()) {
            case "currency": return CommandManager.currency(channel, command);
            case "currencyName": return CommandManager.currencyName(channel, command);
            case "currencyInc": return CommandManager.currencyInc(channel, command);
            case "subEnable": return CommandManager.subEnable(channel, command);
            case "subMultiplier": return CommandManager.subMultiplier(channel, command);
            case "isSub": return CommandManager.isUserSub(channel, sender, command);
        }
        return processUserCommand(channel, sender, command);
        // TODO: Code that precess admin chat commands
    }

    private static String processUserCommand(String channel, String sender, Command command) {
        switch (command.getName()) {
            case "myCurrency": return CommandManager.myCurrency(channel, sender, command);
        }
        return "You are lovely user :3";
        // TODO: Code that precess user chat commands
    }

    private static Command parseCommand(String message) {
        try {
            Lexer lexer = new Lexer(message);
            lexer.tokenize();
            Parser parser = new Parser(lexer.getTokenList());
            parser.start(false);

            int sizeOfStackValues = parser.stackValues.size();
            String commandName = parser.stackValues.get(0).toString();

            Object[] params = new Object[sizeOfStackValues];
            for (int i = 1; i < sizeOfStackValues; i++) {
                params[i-1] = parser.stackValues.get(i);
            }
            return new Command(commandName, params);
        } catch (RuntimeException e) {
            return new Command(e.getMessage(), null);
        }
    }
}

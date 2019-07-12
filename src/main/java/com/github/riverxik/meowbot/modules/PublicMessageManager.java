package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.commands.fsa.Lexer;
import com.github.riverxik.meowbot.commands.fsa.Parser;
import com.github.riverxik.meowbot.modules.bets.BetsManager;
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
        switch (command.getName().toLowerCase()) {
            case "currency": return CommandManager.currency(channel, command);
            case "currencyname": return CommandManager.currencyName(channel, command);
            case "currencyinc": return CommandManager.currencyInc(channel, command);
            case "subenable": return CommandManager.subEnable(channel, command);
            case "submultiplier": return CommandManager.subMultiplier(channel, command);
            case "issub": return CommandManager.isUserSub(channel, sender, command);
        }
        return processUserCommand(channel, sender, command);
        // TODO: Code that precess admin chat commands
    }

    private static String processUserCommand(String channel, String sender, Command command) {
        switch (command.getName().toLowerCase()) {
            case "mycurrency": return CommandManager.myCurrency(channel, sender, command);
            case "addquote": return CommandManager.addQuote(channel, sender, command);
            case "quote": return CommandManager.showQuote(channel, command);
            case "removequote": return CommandManager.removeQuote(channel, sender, command);
            case "help": return "Все команды: https://github.com/Riverxik/MeowBot3/blob/develop/commands.md";
            case "calc": return command.getParameters()[0].toString();
            case "lot": return BetsManager.processLotCommand(channel, sender, command);
            case "bet": return BetsManager.processBetCommand(channel, sender, command);
            case "money": return BetsManager.processMoneyCommand(channel, sender, command);
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

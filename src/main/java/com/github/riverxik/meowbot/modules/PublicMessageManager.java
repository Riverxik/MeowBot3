package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicMessageManager {
    private static final Logger log = LoggerFactory.getLogger(PrivateMessageManager.class);

//    public static String processNewMessage(String channel, String sender, String message) {
//        /*
//         * Принимает сообщение, выбирает куда дальше переслать.
//         * Сообщение может быть получено от админа, либо от пользователей.
//         * Если сообщенние от админа, то обрабатываем все команды.
//         * Если сообщение от пользователя, то обрабатываем только команды для пользователей.
//         *
//         * Получаем отправителя.
//         * Получаем администратора.
//         * Если сообщение от админа передаём в обработчик для админа.
//         * иначе отдаем в обработчик для пользователей.
//         * */
//        AbstractCommand userCommand = parseCommand(message);
//        if(sender.equals(Configuration.admin))
//            return processAdminCommand(channel, sender, userCommand);
//        else
//            return processUserCommand(channel, sender, userCommand);
//    }
//
//    private static String processAdminCommand(String channel, String sender, AbstractCommand command) {
//        switch (command.getName().toLowerCase()) {
//            case "currency": return CommandManager.currency(channel, command);
//            case "currencyname": return CommandManager.currencyName(channel, command);
//            case "currencyinc": return CommandManager.currencyInc(channel, command);
//            case "subenable": return CommandManager.subEnable(channel, command);
//            case "submultiplier": return CommandManager.subMultiplier(channel, command);
//            case "issub": return CommandManager.isUserSub(channel, sender, command);
//        }
//        return processUserCommand(channel, sender, command);
//        // TODO: Code that precess admin chat commands
//    }
//
//    private static String processUserCommand(String channel, String sender, AbstractCommand command) {
//        switch (command.getName().toLowerCase()) {
//            case "mycurrency": return CommandManager.myCurrency(channel, sender, command);
//            case "addquote": return CommandManager.addQuote(channel, sender, command);
//            case "quote": return CommandManager.showQuote(channel, command);
//            case "removequote": return CommandManager.removeQuote(channel, sender, command);
//            case "help": return "Все команды: https://github.com/Riverxik/MeowBot3/blob/develop/commands.md";
//            case "calc": return command.getParameters()[0].toString();
//        }
//        return "You are lovely user :3";
//        // TODO: Code that precess user chat commands
//    }
}

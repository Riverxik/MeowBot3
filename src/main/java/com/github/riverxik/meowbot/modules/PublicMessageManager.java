package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
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

        if(sender.equals(Configuration.admin))
            return processAdminCommand(message);
        else
            return processUserCommand(message);
    }

    private static String processAdminCommand(String message) {
        return processUserCommand(message);
        //return "You are admin :>";
        // TODO: Code that precess admin chat commands
    }

    private static String processUserCommand(String message) {
        try {
            Lexer lexer = new Lexer(message.substring(1));
            lexer.tokenize();
            Parser parser = new Parser(lexer.getTokenList());
            parser.start(false);
            return parser.stackValues.get(0).toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        //return "You are lovely user :3";
        // TODO: Code that precess user chat commands
    }
}

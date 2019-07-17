package com.github.riverxik.meowbot.modules;
import com.github.riverxik.meowbot.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Do a documentation when a class will completed.
public class PrivateMessageManager {

    private static final Logger log = LoggerFactory.getLogger(PrivateMessageManager.class);

    public static String processNewMessage(String sender, String message) {
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
        log.info(String.format("[PM][%s] - [%s]", sender, message));

        if(sender.equals(Configuration.admin))
            return processAdminCommand();
        else
            return processUserCommand();
    }

    private static String processAdminCommand() {
        return "You are admin :>";
        // TODO: Code that precess admin pm commands
    }

    private static String processUserCommand() {
        return "You are lovely user :3";
        // TODO: Code that precess user pm commands
    }
}

# Все команды:
Пользовательские команды:
- [myCurrency](https://github.com/Riverxik/MeowBot3/commands.md#myCurrency) - Показывает текущее значение валюты
- [isSub](https://github.com/Riverxik/MeowBot3/commands.md#isSub) - Показывает является ли зритель подписчиком
- [!quote](https://github.com/Riverxik/MeowBot3/commands.md#quote) - Показывает цитату из базы данных

Команды для модераторов/подписчиков/vip:
- [!addQuote](https://github.com/Riverxik/MeowBot3/commands.md#addQuote) - Добавляет новую цитату в базу данных для текущего канала

Команды для админа:
- [currency](https://github.com/Riverxik/MeowBot3/commands.md#currency) - Статус модуля валюты
- [currencyName](https://github.com/Riverxik/MeowBot3/commands.md#currencyName) - Наименование валюты
- [currencyInc](https://github.com/Riverxik/MeowBot3/commands.md#currencyInc) - Значение прибавки валюты
- [subEnable](https://github.com/Riverxik/MeowBot3/commands.md#subEnable) - Статус множителя для подписчиков
- [subMultiplier](https://github.com/Riverxik/MeowBot3/commands.md#subMultiplier) - Значение множителя для подписчиков
- [removeQuote](https://github.com/Riverxik/MeowBot3/commands.md#removeQuote) - Удаляет цитату из базы данных канала
---

## !currency
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L12)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currency [String]|!currency|Показывает статус модуля валют на текущем канале|Админ|
||!currency on|Включает модуль валюты на текущем канале|Админ|
||!currency off|Выключает модуль валюты на текущем канале|Админ|
||!currency help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !currencyName
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L36)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currencyName [String]|!currencyName "Буткоины"|Переименовывает валюту для текущего канала|Админ|
||!currencyName help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !currencyInc
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L55)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currencyInc [Int]|!currencyInc 2|Количество валюты, которую выдаёт бот каждые 5 минут|Админ|
||!currencyInc help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !subEnable
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L84)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!subEnable [String]|!subEnable|Показывает статус множителя для подписчиков/vip|Админ|
||!subEnable on|Включает множитель для подписчиков/vip|Админ|
||!subEnable off|Выключает множитель для подписчиков/vip|Админ|
||!subEnable help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !subMultiplier
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L109)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!subMultiplier [Int]|!subMultiplier|Показывает значение множителя для подписчиков/vip|Админ|
||!subMultiplier 5|Устанавливает множитель для подписчиков равным `5`|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !myCurrency
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L142)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!myCurrency [String]|!myCurrency|Показывает ваше текущее значение валюты|Все пользователи
||!myCurrency byterbrodtv|Показывает значение валюты пользователя `byterbrodtv`|Все пользователи
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !isSub
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L163)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!isSub [String]|!isSub|Показывает является ли зритель подписчиком|Все пользователи
||!isSub byterbrodtv|Показывает является ли `byterbrodtv` подписчиком|Все пользователи
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !quote
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L204)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!quote [int]|!quote 2|Выводит в чат цитату с id = `2`|Все пользователи
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !addQuote
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L182)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!addQuote [String]|!quote "Текст цитаты"|Записывает `Текст цитаты` в базу данных|Модератор, Подписчик, Vip
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---
## !removeQuote
[source](https://github.com/Riverxik/MeowBot3/blob/fea7568eae45a6f6e81daec8c004b906921f4ac2/src/main/java/com/github/riverxik/meowbot/modules/CommandManager.java#L224)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!removeQuote [Int]|!removeQuote 3|Удаляет цитату с id = `3` для текущего канала|Админ, Владелец канала
[Наверх](https://github.com/Riverxik/MeowBot3/commands.md#все-команды)
---

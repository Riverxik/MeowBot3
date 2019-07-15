# Все команды:
Пользовательские команды:
- [!currency](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency) - Показывает текущее значение валюты
- [!quote](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#quote) - Показывает цитату из базы данных

Команды для модераторов:
- [!quote add](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#quote-add) - Добавляет новую цитату в базу данных для текущего канала

Команды для админа:
- [!currency](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency) - Статус модуля валюты
- [!currency name](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-name) - Наименование валюты
- [!currency inc](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-inc) - Значение прибавки валюты
- [!currency sub](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-sub) - Статус множителя для подписчиков
- [!currency sub inc](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-sub-inc) - Значение множителя для подписчиков
- [!quote remove](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#quote-remove) - Удаляет цитату из базы данных канала
---

## !currency
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencyStatusHandler.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currency [String]|!currency|Показывает статус модуля валют на текущем канале|Админ|
||!currency on|Включает модуль валюты на текущем канале|Админ|
||!currency off|Выключает модуль валюты на текущем канале|Админ|
||!currency help|Показывает небольшую справку по синтаксису|Админ|
|!currency [String]|!currency|Показывает текущее значение валюты|Все пользователи|
|!currency [String]|!currency test|Показывает текущее значение валюты пользователя `test`|Все пользователи|
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency name
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencyNameHandler.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currency name [String]|!currencyName "Буткоины"|Переименовывает валюту для текущего канала|Админ|
|!currency name|!currencyName|Выводит текущее значение названия валюты|Админ|
||!currency name help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency inc
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencyIncHandler.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currency inc [Int]|!currencyInc|Показывает текущее значение|Админ|
|!currency inc|!currencyInc 2|Количество валюты, которую выдаёт бот каждые 5 минут|Админ|
||!currency inc help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency sub
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencySubEnableHandler.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currency sub [String]|!subEnable|Показывает статус множителя для подписчиков/vip|Админ|
||!currency sub on|Включает множитель для подписчиков/vip|Админ|
||!currency sub off|Выключает множитель для подписчиков/vip|Админ|
||!currency sub help|Показывает небольшую справку по синтаксису|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency sub inc
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencySubMultiplierHandler.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!currency sub inc [Int]|!subMultiplier|Показывает значение множителя для подписчиков/vip|Админ|
||!currency sub inc 5|Устанавливает множитель для подписчиков равным `5`|Админ|
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !quote
[source](src/main/java/com/github/riverxik/meowbot/modules/quotes/commands/QuoteHandle.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!quote [int]|!quote 2|Выводит в чат цитату с id = `2`|Все пользователи
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !quote add
[source](src/main/java/com/github/riverxik/meowbot/modules/quotes/commands/AddQuoteHandle.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!quote add [String]|!quote add "Текст цитаты"|Записывает `Текст цитаты` в базу данных|Модератор
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !quote remove
[source](src/main/java/com/github/riverxik/meowbot/modules/quotes/commands/RemoveQuoteHandle.java)

|Синтаксис|Пример|Описание|Доступность|
|---|---|---|---|
|!quote remove [Int]|!quote remove 3|Удаляет цитату с id = `3` для текущего канала|Админ, Владелец канала
[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---

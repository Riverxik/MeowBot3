# Все команды
Пользовательские команды:
  - [!currency](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency) - Показывает текущее значение валюты
  - [!quote](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#quote) - Показывает цитату из базы данных
  - [!roll](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#roll) - Игровой автомат для игры на валюту
  - [!alias list](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#alias-list) - Показ всех алиасов для команды
  - [!encrypt](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#encrypt) - Шифрует сообщение пользователя
  - [!duel](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#encrypt) - Пользовательские дуэли

Команды для VIP/SUB пользователей:
  - [!quote add](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#quote-add) - Добавляет новую цитату в базу данных для текущего канала

Команды для модераторов:
  - [!alias add](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#alias-add) - Добавляет новый алиас к команде для текущего канала

Команды для админа:
  - [!currency](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency) - Статус модуля валюты
  - [!currency name](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-name) - Наименование валюты
  - [!currency inc](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-inc) - Значение прибавки валюты
  - [!currency sub](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-sub) - Статус множителя для подписчиков
  - [!currency sub inc](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#currency-sub-inc) - Значение множителя для подписчиков
  - [!quote remove](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#quote-remove) - Удаляет цитату из базы данных канала
  - [!alias remove](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#alias-remove) - Удаляет алиас из базы данных канала
  - [!cmd](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#cmd) - Команда для управления кастомными командами
  - [!cooldown](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#cooldown) - Устанавливает задержку к команде перед следующим использованием
  
---

## !currency
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencyStatusHandler.java)

|     Синтаксис    |     Пример     |                    Описание                          |Доступность     |
|------------------|----------------|------------------------------------------------      |-----------     |
|!currency [String]|!currency       |Показывает статус модуля валют на текущем канале      |   Админ        |
|                  |!currency on    |Включает модуль валюты на текущем канале              |   Админ        |
|                  |!currency off   |Выключает модуль валюты на текущем канале             |Админ           |
|                  |!currency help  |Показывает небольшую справку по синтаксису            |Админ           |
|                  |!currency       |Показывает текущее значение валюты                    |Все пользователи|
|                  |!currency test  |Показывает текущее значение валюты пользователя `test`|Все пользователи|

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency name
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencyNameHandler.java)

|Синтаксис                  |Пример                     |Описание                                   |Доступность|
|---                        |---                        |---                                        |---        |
|!currency name [String]    |!currency name "Буткоины"  |Переименовывает валюту для текущего канала |Админ      |
|                           |!currency name             |Выводит текущее значение названия валюты   |Админ      |
|                           |!currency name help        |Показывает небольшую справку по синтаксису |Админ      |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency inc
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencyIncHandler.java)

|Синтаксис              |Пример             |Описание                                               |Доступность|
|---                    |---                |---                                                    |---        |
|!currency inc [Int]    |!currency inc      |Показывает текущее значение                            |Админ      |
|                       |!currency inc 2    |Количество валюты, которую выдаёт бот каждые 5 минут   |Админ      |
|                       |!currency inc help |Показывает небольшую справку по синтаксису             |Админ      |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency sub
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencySubEnableHandler.java)

|Синтаксис              |Пример             |Описание                                       |Доступность|
|---                    |---                |---                                            |---        |
|!currency sub [String] |!currency sub      |Показывает статус множителя для подписчиков/vip|Админ      |
|                       |!currency sub on   |Включает множитель для подписчиков/vip         |Админ      |
|                       |!currency sub off  |Выключает множитель для подписчиков/vip        |Админ      |
|                       |!currency sub help |Показывает небольшую справку по синтаксису     |Админ      |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !currency sub inc
[source](src/main/java/com/github/riverxik/meowbot/modules/currency/commands/CurrencySubMultiplierHandler.java)

|Синтаксис              |Пример             |Описание                                           |Доступность|
|---                    |---                |---                                                |---        |
|!currency sub inc [Int]|!currency sub inc  |Показывает значение множителя для подписчиков/vip  |Админ      |
|                       |!currency sub inc 5|Устанавливает множитель для подписчиков равным `5` |Админ      |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !quote
[source](src/main/java/com/github/riverxik/meowbot/modules/quotes/commands/QuoteHandle.java)

|Синтаксис      |Пример     |Описание                       |Доступность        |
|---            |---        |---                            |---                |
|!quote [int]   |!quote 2   |Выводит в чат цитату с id = `2`|Все пользователи   |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
---
## !roll
[source](src/main/java/com/github/riverxik/meowbot/modules/SlotMachineCommandHandler.java)

|Синтаксис      |Пример     |Описание                       |Доступность        |
|---            |---        |---                            |---                |
|!roll [int]    |!roll 5    |Запускает иговой автомат		|Все пользователи   |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !quote add
[source](src/main/java/com/github/riverxik/meowbot/modules/quotes/commands/AddQuoteHandle.java)

|Синтаксис          |Пример                     |Описание                               |Доступность|
|---                |---                        |---                                    |---        |
|!quote add [String]|!quote add "Текст цитаты"  |Записывает `Текст цитаты` в базу данных|VIP/SUB    |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !quote remove
[source](src/main/java/com/github/riverxik/meowbot/modules/quotes/commands/RemoveQuoteHandle.java)

|Синтаксис          |Пример         |Описание                                       |Доступность            |
|---                |---            |---                                            |---                    |
|!quote remove [Int]|!quote remove 3|Удаляет цитату с id = `3` для текущего канала  |Админ, Владелец канала |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !alias add
[source](src/main/java/com/github/riverxik/meowbot/modules/alias/AddAliasCommandHandler.java)

|Синтаксис                          |Пример                     |Описание                                   |Доступность|
|---                                |---                        |---                                        |---        |
|!alias add [aliasName] [cmdName]   |!alias add помощь help     |Привязывает алиас `помощь` к команде !help |Модератор  |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !alias remove
[source](src/main/java/com/github/riverxik/meowbot/modules/alias/RemoveAliasCommandHandler.java)

|Синтаксис                  |Пример              |Описание                   |Доступность            |
|---                        |---                 |---                        |---                    |
|!alias remove [aliasName]  |!alias remove помощь|Удаляет алиас `помощь`     |Админ, Владелец канала |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !alias list
[source](src/main/java/com/github/riverxik/meowbot/modules/alias/ShowAllAliasForCommandHandler.java)

|Синтаксис             |Пример              |Описание                               |Доступность            |
|---                   |---                 |---                                    |---                    |
|!alias list [String]  |!alias list help    |Показывает все алиасы к команде `help` |Все пользователи       |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !cmd
[source](src/main/java/com/github/riverxik/meowbot/modules/custom_commands/CommandHandler.java)

|Синтаксис                      |Пример                     |Описание                                                                           |Доступность            |
|---                            |---                        |---                                                                                |---                    |
|!cmd [String]                  |!cmd test                  |Показывает синтаксис команды `test`                                                |Админ, Владелец канала |
|!cmd add [cmdName] [String]    |!cmd add test "Test"       |Добавляет новую команду `test`                                                     |Админ, Владелец канала |
|!cmd update [cmdName] [String] |!cmd update test "Test2"   |Обновляет значение команды `test`                                                  |Админ, Владелец канала |
|!cmd remove [String]           |!cmd remove test           |Удаляет команду `test`                                                             |Админ, Владелец канала |
|!cmd list [public]             |!cmd list [public]         |Показывает список кастомных команд. Если указан `public` - пишет в чат, иначе в лс.|Все пользователи       |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !encrypt
[source](src/main/java/com/github/riverxik/meowbot/commands/EncryptCommandHandler.java)

|Синтаксис                  |Пример              |Описание                               |Доступность            |
|---                        |---                 |---                                    |---                    |
|!encrypt [String] [Int]    |!encrypt "Тест" 10  |Шифрует `Тест` со сдвигом `10`         |Все пользователи       |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !cooldown
[source](src/main/java/com/github/riverxik/meowbot/commands/CooldownCommandHandle.java)

|Синтаксис                  |Пример              |Описание                                                          |Доступность            |
|---                        |---                 |---                                                               |---                    |
|!cooldown [cmdName] [Int]  |!cooldown help 10   |Устанавливает кулдаун к команде `help` со значением `10` секунд   |Админ, Владелец канала |

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---
## !duel
[source](src/main/java/com/github/riverxik/meowbot/modules/duel/DuelCommandHandler.java)

|Синтаксис              |Пример              	|Описание                                                       |Доступность        |
|---                    |---                 	|---                                                            |---                |
|!duel [name] [amount]	|!duel byterbrodtv 20	|Вызывает пользователя `byterbrodtv` на дуэль стоимостью `20`	|Все пользователи	|
|!duel accept [name]	|!duel accept user		|Принимает дуэль от пользователя `user`							|Все пользователи	|
|!duel deny [name]		|!duel deny user		|Отклоняет текущий вызов на дуэль								|Все пользователи	|
|!duel cancel			|!duel cancel			|Отменяет активный вызов на дуэль								|Все пользователи	|

[Наверх](https://github.com/Riverxik/MeowBot3/blob/master/commands.md#все-команды)
---

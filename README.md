# Java bot for [Twitch](https://twitch.tv) - MeowBot :3
## Description
  Chat-bot for twitch which includes a lot of custom features.
  
|Master|Develop|Code Quality
|---|---|---|
| [![Build Status](https://travis-ci.org/Riverxik/MeowBot3.svg?branch=master)](https://travis-ci.org/Riverxik/MeowBot3)  | [![Build Status](https://travis-ci.org/Riverxik/MeowBot3.svg?branch=develop)](https://travis-ci.org/Riverxik/MeowBot3) | [![Codacy Badge](https://api.codacy.com/project/badge/Grade/72ab0cb044c64bf6abc709f6f945f9fd)](https://www.codacy.com/app/Riverxik/MeowBot3?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Riverxik/MeowBot3&amp;utm_campaign=Badge_Grade) |

## Installation
  Bot isn't complete for now. I'm still developing it.
  You can try it already - just compile and run :)

## Quick start
  - Start a bot with start.bat:

  ```cmd
  @java -jar meowbot-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```
  
  - Change config.json

  ```json
  { 
    "admin": "PutYourNickNameHere",
    "tokens": {
        "chatToken": "PutYoursHere",
        "clientAppId": "PutYoursHere",
        "clientAppSecret": "PutYoursHere"
    }
  }
  ```
  
  - You can get you 'chatToken' from [Twitch chat password](https://twitchapps.com/tmi/)

  - 'clientAppId' and 'clientAppSecret' from [Twitch dev dashboard](https://dev.twitch.tv/)

  - The rest of the settings are adjusted as you need
 
## Used libraries
- [Twitch4j](https://github.com/twitch4j/twitch4j) - The core of twitch: @twitch4j
- [SQLite](https://www.sqlite.org/index.html) - I use this library for work with database

## Features
  With this bot you can manage your twitch chat!
- [x] Supports any number of channels.
- [x] Currency system.
- [x] Quote system.
- [x] Duel system.
- [x] Custom commands for any channel (For any wanted group: viewers, mods, subscribers).
- [x] Aliases for any commands.
- [ ] Bet system: play bets, win prize, make your channel chat more active! :>
- [ ] Chat notifications: when stream goes live, sub/resub event, cheers, raids.
- [ ] Statistic for your stream.
## License
----
MIT
**Free Software, Hell Yeah!**

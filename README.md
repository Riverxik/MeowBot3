# Java bot for [Twitch](https://twitch.tv) - MeowBot :3
## Description
----
  New version of my java bot for Twitch (supports new twitch api). 
  My last version of this bot was support only api v.3 which is deprecated on twitch and wouldn't work soon.
  So, i had to change the api of the new version of bot and here it is.
  Now i can add more some features which wasn't available on latest api.
  
|Master|Develop|
|---|---|
| [![Build Status](https://travis-ci.org/Riverxik/MeowBot3.svg?branch=master)](https://travis-ci.org/Riverxik/MeowBot3)  | [![Build Status](https://travis-ci.org/Riverxik/MeowBot3.svg?branch=develop)](https://travis-ci.org/Riverxik/MeowBot3) |

## Installation
----
  Bot isn't complete for now. I'm still developing it.
  You can try it already - just compile and run :)

## Quick start
----
- Start a bot with `start.bat`:
  ```cmd
  @java -jar meowbot-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```
- Change `config.json`
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
- You can get you `chatToken` from [Twitch chat password](https://twitchapps.com/tmi/)
- `clientAppId` and `clientAppSecret` from [Twitch dev dashboard](https://dev.twitch.tv/)
- The rest of the settings are adjusted as you need
 
## Used libraries
----
  - [Twitch4j](https://github.com/twitch4j/twitch4j) - The core of twitch: @twitch4j
  - [SQLite](https://www.sqlite.org/index.html) - I use this library for work with database

## Features
----
  With this bot you can manage your twitch chat!
  - [x] Supports any number of channels.
  - [x] Currency system.
  - [ ] Custom commands for any channel (For any wanted group: viewers, mods, subscribers).
  - [ ] Bet system: play bets, win prize, make your channel chat more active! :>
  - [ ] Chat notifications: when stream goes live, sub/resub event, cheers, raids.
  - [ ] Statistic for your stream.
## License
----
MIT
**Free Software, Hell Yeah!**

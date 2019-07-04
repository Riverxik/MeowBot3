package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.database.ChannelDb;
import com.github.riverxik.meowbot.database.ChannelUsers;

public class CommandManager {

    public static String currency(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return currencyStatusForChannel(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            String param = String.valueOf(objParam[0]);
            if("on".equals(param.toLowerCase())) {
                return changeCurrencyStatusForChannel(channelName, true);
            }
            if("off".equals(param.toLowerCase())) {
                return changeCurrencyStatusForChannel(channelName, false);
            }
            if("help".equals(param.toLowerCase())) {
                return "Shows currency status. Use !currency [on/off]";
            }
            return String.format("Unknown parameter %s", param);
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String currencyName(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getCurrencyName(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            String newName = String.valueOf(objParam[0]);
            if("help".equals(newName.toLowerCase())) {
                return "Shows currency name. Use !currencyName [newValue]";
            }
            return changeCurrencyNameForChannel(channelName, newName);
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String currencyInc(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getCurrencyInc(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            if(objParam[0] instanceof String) {
                String param = String.valueOf(objParam[0]);
                if("help".equals(param.toLowerCase())) {
                    return "Shows currency increment. Use !currencyInc [newIntValue]";
                }
            }
            if(objParam[0] instanceof Integer) {
                if(isValidInteger(objParam[0])) {
                    int inc = (int) objParam[0];
                    return changeCurrencyIncForChannel(channelName, inc);
                } else {
                    return "Illegal integer number";
                }
            }
            return "Please use integer as parameter!";
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String subEnable(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getSubEnable(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            String param = String.valueOf(objParam[0]);
            if("on".equals(param.toLowerCase())) {
                return changeSubEnableForChannel(channelName, true);
            }
            if("off".equals(param.toLowerCase())) {
                return changeSubEnableForChannel(channelName, false);
            }
            if("help".equals(param.toLowerCase())) {
                return "Shows subscriber multiplier status. Use !subEnable [on/off]";
            }
            return String.format("Unknown parameter %s", param);
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String subMultiplier(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getSubMultiplier(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            if(objParam[0] instanceof String) {
                String param = String.valueOf(objParam[0]);
                if("help".equals(param.toLowerCase())) {
                    return "Shows multiplier applied to subscribers and vips. Use !subMultiplier [newIntValue]";
                }
            }
            if(objParam[0] instanceof Integer) {
                if(isValidInteger(objParam[0])) {
                    int inc = (int) objParam[0];
                    if(CurrencyManager.setChannelSubMultiplier(channelName, inc) == 1) {
                        return String.format("Subscriber multiplier for %s is [%d]", channelName, inc);
                    } else {
                        return String.format("Subscriber multiplier couldn't updated!");
                    }
                } else {
                    return "Illegal integer number";
                }
            }
            return "Please use integer as parameter!";
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String myCurrency(String channelName, String sender, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getUserCurrency(channelName, sender);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            if(objParam[0] instanceof String) {
                String param = String.valueOf(objParam[0]);
                if("help".equals(param.toLowerCase())) {
                    return "Shows user currency. Use !myCurrency or !myCurrency [userName]";
                }
                return getUserCurrency(channelName, param);
            } else return "Please use string as a parameter!";
        } else return String.format("Can't recognize command with %d parameters", paramLength);
    }

    private static String getUserCurrency(String channelName, String userName) {
        int amount = ChannelUsers.getUserCurrency(channelName, userName);
        String currency = CurrencyManager.getChannelCurrencyName(channelName);
        if (amount != 0)
            return String.format("%s %s = %d", currency, userName, amount);
        else
            return String.format("Can't find amount of currency %s", userName);
    }

    private static String getSubMultiplier(String channelName) {
        return String.format("Subscribers multiplier for %s is [%d]",channelName,
                CurrencyManager.getChannelSubMultiplier(channelName));
    }

    private static String getSubEnable(String channelName) {
        if(CurrencyManager.getChannelSubEnable(channelName))
            return String.format("Subscribers multiplier for %s is [enable]", channelName);
        else
            return String.format("Subscribers multiplier for %s is [disable]", channelName);
    }

    private static String changeSubEnableForChannel(String channelName, boolean isEnable) {
        if(CurrencyManager.setChannelSubEnable(channelName, isEnable)) {
            if(isEnable)
                return String.format("Now subscribers multiplier for %s is [enable]", channelName);
            else
                return String.format("Now subscribers multiplier for %s is [disable]", channelName);
        } else {
            return String.format("Subscribers multiplier for %s couldn't updated", channelName);
        }
    }

    private static boolean isValidInteger(Object integer) {
        try {
            int value = (int) integer;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String changeCurrencyIncForChannel(String channelName, int newInc) {
        if(CurrencyManager.setChannelCurrencyInc(channelName, newInc) == 1)
            return getCurrencyInc(channelName);
        else
            return String.format("Currency increment wasn't updated");
    }

    private static String getCurrencyInc(String channelName) {
        return String.format("Current currency increment for %s is [%s]",
                channelName, CurrencyManager.getChannelCurrencyInc(channelName));
    }

    private static String getCurrencyName(String channelName) {
        return String.format("Current currency name for %s is [%s]",
                channelName, CurrencyManager.getChannelCurrencyName(channelName));
    }

    private static String changeCurrencyNameForChannel(String channelName, String newCurrencyName) {
        return CurrencyManager.setChannelCurrencyName(channelName, newCurrencyName);
    }

    private static String changeCurrencyStatusForChannel(String channelName, boolean isEnable) {
        for (ChannelDb channel : Configuration.loadingChannels) {
            if(channelName.equals(channel.getChannelName())) {
                channel.setCurrencyEnabled(isEnable);
            }
        }
        if(isEnable)
            return "Channel currency now enabled!";
        else
            return "Channel currency now disabled!";
    }

    private static String currencyStatusForChannel(String channelName) {
        for (ChannelDb channel : Configuration.loadingChannels) {
            if(channelName.equals(channel.getChannelName())) {
                return String.format("Currency for %s is %s", channelName, channel.isCurrencyEnabled());
            }
        }
        return String.format("Couldn't find currency status for %s", channelName);
    }
}

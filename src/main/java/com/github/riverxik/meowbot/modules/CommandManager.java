package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.modules.bets.BetsManager;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelUser;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;

public class CommandManager {

    public static String currency(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return currencyStatusForChannel(channelName);
        }
        if (paramLength == 1) {
            if(Configuration.isCurrencyEnable()) {
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
            } else return ErrorCodes.DISABLED_CURRENCY.getInfo();
        } else
            return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();
    }

    public static String currencyName(String channelName, Command command) {
        if (Configuration.isCurrencyEnable()) {
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

            } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.DISABLED_CURRENCY.getInfo();
    }

    public static String currencyInc(String channelName, Command command) {
        if (Configuration.isCurrencyEnable()) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return getCurrencyInc(channelName);
            }
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String) {
                    String param = String.valueOf(objParam[0]);
                    if ("help".equals(param.toLowerCase())) {
                        return "Shows currency increment. Use !currencyInc [newIntValue]";
                    }
                }
                if (objParam[0] instanceof Integer) {
                    if (isValidInteger(objParam[0])) {
                        int inc = (int) objParam[0];
                        return changeCurrencyIncForChannel(channelName, inc);
                    } else {
                        return "Illegal integer number";
                    }
                }
                return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_INT.getInfo();

            } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.DISABLED_CURRENCY.getInfo();
    }

    public static String subEnable(String channelName, Command command) {
        if (Configuration.isCurrencyEnable()) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return getSubEnable(channelName);
            }
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                String param = String.valueOf(objParam[0]);
                if ("on".equals(param.toLowerCase())) {
                    return changeSubEnableForChannel(channelName, true);
                }
                if ("off".equals(param.toLowerCase())) {
                    return changeSubEnableForChannel(channelName, false);
                }
                if ("help".equals(param.toLowerCase())) {
                    return "Shows subscriber multiplier status. Use !subEnable [on/off]";
                }
                return String.format("Unknown parameter %s", param);

            } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.DISABLED_CURRENCY.getInfo();
    }

    public static String subMultiplier(String channelName, Command command) {
        if (Configuration.isCurrencyEnable()) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return getSubMultiplier(channelName);
            }
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String) {
                    String param = String.valueOf(objParam[0]);
                    if ("help".equals(param.toLowerCase())) {
                        return "Shows multiplier applied to subscribers and vips. Use !subMultiplier [newIntValue]";
                    }
                }
                if (objParam[0] instanceof Integer) {
                    if (isValidInteger(objParam[0])) {
                        int inc = (int) objParam[0];
                        if (CurrencyManager.setChannelSubMultiplier(channelName, inc) == 1) {
                            return String.format("Subscriber multiplier for %s is [%d]", channelName, inc);
                        } else {
                            return "Subscriber multiplier couldn't updated!";
                        }
                    } else {
                        return "Illegal integer number";
                    }
                }
                return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_INT.getInfo();

            } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.DISABLED_CURRENCY.getInfo();
    }

    public static String myCurrency(String channelName, String sender, Command command) {
        if (Configuration.isCurrencyEnable()) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return getUserCurrency(channelName, sender);
            }
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String) {
                    String param = String.valueOf(objParam[0]);
                    if ("help".equals(param.toLowerCase())) {
                        return "Shows user currency. Use !myCurrency or !myCurrency [userName]";
                    }
                    return getUserCurrency(channelName, param);
                } else return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();

            } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.DISABLED_CURRENCY.getInfo();
    }

    public static String isUserSub(String channelName, String sender, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            boolean isSub = Configuration.getChannelByName(channelName).getChannelUserByName(sender).isSub();
            return isSub ? String.format("%s is subscriber!", sender) : String.format("%s isn't subscriber!", sender);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            if (objParam[0] instanceof String) {
                String param = String.valueOf(objParam[0]);
                if("help".equals(param.toLowerCase())) {
                    return "Use !isSub [userName]";
                }
                boolean isSub = Configuration.getChannelByName(channelName).getChannelUserByName(param).isSub();
                return isSub ? String.format("%s is subscriber!", param) : String.format("%s isn't subscriber!", param);
            } else return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();
        } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();
    }

    public static String addQuote(String channelName, String sender, Command command) {
        ChannelUser user = Configuration.getChannelByName(channelName).getChannelUserByName(sender);
        if (user.isMod() || user.isVip() || user.isSub() || channelName.equals(sender)) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String) {
                    String param = String.valueOf(objParam[0]);
                    int quoteId = Configuration.getChannelByName(channelName).getSettings().getQuotes().addQuote(param);
                    if (quoteId != -1) {
                        return String.format("New quote #%d has been added!", quoteId);
                    } else {
                        return ErrorCodes.QUOTE_NOT_ADDED.getInfo();
                    }

                } else return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();

            } else return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.ACCESS_DENIED.getInfo();
    }

    public static String showQuote(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            // TODO: Maybe i should add showing random quote.
            return "Use !quote [id]. For example: !quote 2";
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            if (objParam[0] instanceof Integer) {
                if (isValidInteger(objParam[0])) {
                    int param = (int) objParam[0];
                    return QuotesManager.showQuote(channelName, param);

                } else ErrorCodes.ILLEGAL_NUMBER.getInfo();

            } else ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();
        }
        return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();
    }

    public static String removeQuote(String channelName, String sender, Command command) {
        // This command available only for channel owner / bot's admin
        if(channelName.equals(sender) || Configuration.admin.equals(sender)) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return "Use !removeQuote [quoteId]. For example: !removeQuote 2";
            }
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof Integer) {
                    if(isValidInteger(objParam[0])) {
                        int param = (int) objParam[0];
                        if(QuotesManager.removeQuote(channelName, param))
                            return String.format("Quote with id = [%d] has been removed!", param);

                        else return ErrorCodes.QUOTE_NOT_REMOVED.getInfo();

                    } else return ErrorCodes.ILLEGAL_NUMBER.getInfo();

                } else return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_INT.getInfo();
            }
            return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.ACCESS_DENIED.getInfo();
    }

    public static boolean isValidInteger(Object integer) {
        try {
            int value = (int) integer;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String getUserCurrency(String channelName, String userName) {
        int amount = CurrencyManager.getUserCurrency(channelName, userName);
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

    private static String changeCurrencyIncForChannel(String channelName, int newInc) {
        if(CurrencyManager.setChannelCurrencyInc(channelName, newInc) == 1)
            return getCurrencyInc(channelName);
        else
            return "Currency increment wasn't updated";
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
        Configuration.getChannelByName(channelName).getSettings().setCurrencyEnabled(isEnable);
        if(isEnable)
            return "Channel currency now enabled!";
        else
            return "Channel currency now disabled!";
    }

    private static String currencyStatusForChannel(String channelName) {
        if(Configuration.isCurrencyEnable()) {
            boolean status = Configuration.getChannelByName(channelName).getSettings().isCurrencyEnabled();
            return String.format("Currency status for [%s] is %s", channelName, status);
        }
        return "Currency is disabled for all channels!";
    }
}

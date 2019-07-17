package com.github.riverxik.meowbot.commands.fsa;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private String inputString = null;

    private int position = 0;
    private static final char COMMAND_SYMBOL = '!';
    private static final String AVAILABLE_SYMBOLS = "+-*/^'(),?~><;=";
    private static final String AVAILABLE_SYMBOLS_IN_WORDS = "'%";
    private List<Token> tokenList = new ArrayList<>();
    public Lexer(String inputString) {
        this.inputString = inputString+" "+'\0';
    }

    public List<Token> getTokenList() { return this.tokenList; }

    public void tokenize(boolean debug) {
        while(true) {
            char current = getCurrent();
            if(COMMAND_SYMBOL == current)
                tokenList.add(tokenizeCommandSymbol());
            if('\"' == current)
                tokenList.add(tokenizeLongString());
            if(Character.isLetter(current))
                tokenList.add(tokenizeString(current));
            if(Character.isDigit(current))
                tokenList.add(tokenizeDigit(current));
            if(AVAILABLE_SYMBOLS.indexOf(current) != -1)
                tokenList.add(tokenizeSymbol(current));
            if(current == '\0')
                break;
        }
        tokenList.add(new Token(";"));
        if (debug) showAllTokens();
    }

    private Token tokenizeSymbol(char symbol) {
        char nextCurrent = getCurrent();
        Token symbolToken;
        if(nextCurrent == '=') {
            symbolToken = new Token("==");
            symbolToken.setStringValue("==");
        } else {
            position--;
            symbolToken = new Token(Character.toString(symbol));
            symbolToken.setStringValue(Character.toString(symbol));
        }
        return symbolToken;
    }

    private Token tokenizeLongString() {
        char nextSymbol = getCurrent();
        String value = "";
        while(nextSymbol != '\"') {
            if(nextSymbol == '\0')
                break;
            value += nextSymbol;
            nextSymbol = getCurrent();
        }
        Token longStringToken = new Token("STRING");
        longStringToken.setStringValue(value);
        return longStringToken;
    }

    public void showAllTokens() {
        for (Token token :
                tokenList) {
            System.out.println(token.toString());
        }
    }

    private Token tokenizeCommandSymbol() {
        Token symbolToken = new Token("COMMAND");
        symbolToken.setStringValue("!");
        return symbolToken;
    }

    private Token tokenizeString(char first) {
        String value = "" + first;
        while (true) {
            char current = getCurrent();
            if(Character.isLetterOrDigit(current) || AVAILABLE_SYMBOLS_IN_WORDS.indexOf(current) != -1)
                value += current;
            else {
                position--;
                Token stringToken = new Token("STRING");
                stringToken.setStringValue(value);
                return stringToken;
            }
        }
    }

    private Token tokenizeDigit(char first) {
        String value = "" + first;
        boolean isDouble = false;
        while (true) {
            char current = getCurrent();
            if (Character.isDigit(current) || '.' == current) {
                if (!isDouble && '.' == current) {
                    isDouble = true;
                    value += '.';
                }
                if (Character.isDigit(current)) {
                    value += current;
                }
            } else {
                position--;
                if (isDouble) {
                    Token floatToken = new Token("FLOATNUM");
                    floatToken.setFloatValue(Float.valueOf(value));
                    return floatToken;
                } else {
                    Token intToken = new Token("INTEGERNUM");
                    int tmp;
                    try {
                        tmp = Integer.valueOf(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Integer number is not in integer range");
                    }
                    intToken.setIntValue(tmp);
                    return intToken;
                }
            }
        }
    }

    private char getCurrent() {
        return this.inputString.charAt(position++);
    }
}

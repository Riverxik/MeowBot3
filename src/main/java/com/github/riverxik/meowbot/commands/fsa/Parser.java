package com.github.riverxik.meowbot.commands.fsa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
    /** Лист входящих токенов */
    private List<Token> tokenList;
    /** Стэк парсера */
    private List<String> storeStack;
    /** Позиция текущего токена */
    private int position;
    /** Стэк значений */
    public List<Object> stackValues;
    /** Таблица правил */
    private List<Rule> rules;

    private static final List<String> actions = new ArrayList<>();

    public Parser(List<Token> tokenList) {
        this.tokenList = tokenList;

        this.storeStack = new ArrayList<>();
        this.position = 0;
        this.stackValues = new ArrayList<>();

        this.rules = new ArrayList<>();

        initialization();
    }

    private void initialization() {
        loadRules();

        storeStack.add("BOTTOM"); // Дно стека
        storeStack.add(rules.get(0).getName());

        // TODO: Мне лень, но надо добавить импорт действий из файла
        actions.add("eval");
        actions.add("op+");
        actions.add("op-");
        actions.add("op*");
        actions.add("op/");
        actions.add("op^");
        actions.add("--");
        actions.add("++");
        actions.add("less");
        actions.add("more");
        actions.add("equal");
    }

    public void start(boolean isDebug) {
        if(isDebug) {
            System.out.println("\nRules:");
            showAllRules();
            System.out.println("******************");
        }
        while (true)
        {
            if(!parse()) {
                break;
            }
        }
        if("BOTTOM".equals(topStack()) && isDebug)
            System.out.println("Code is valid");
    }

    private boolean parse() {
        String currentToken = getToken();
        String topToken = topStack();
        if(!currentToken.equals(topToken)) {
            if(actions.contains(topToken)) {
                action(topToken);
                return parse();
            }

            if (topToken.equals(rules.get(0).getName()) && "ENDOFFILE".equals(currentToken)) {
                popStack();
                return false;
            }

            if ("BOTTOM".equals(topToken)) {
                if(";".equals(currentToken)) {
                    //pushParamsToStack(rules.get(0).getName());
                    //nextToken();
                    //return parse();
                    return false;
                }
            } else {
                foundNextRule(currentToken, topToken);
                return parse();
            }
        } else {
            switch (topToken) {
                case ",":
                case "+":
                case "-":
                case "*":
                case "/":
                case "<":
                case ">":
                case "==":
                case "=":
                case "^":
                case "(":
                case ")":
                case "~":
                case "COMMAND": // !
                {
                    nextToken();
                    popStack();
                    return parse();
                }
                case ";":
                {
                    nextToken();
                    popStack();
                    return parse();
                }
                default: break;
            }
        }
        return false;
    }

    private void action(String topToken) {
        switch (topToken) {
            case "eval": evaluation(); break;
            case "op-": operation('-'); break;
            case "op+": operation('+'); break;
            case "op*": operation('*'); break;
            case "op/": operation('/'); break;
            case "op^": operation('^'); break;
            case "--": operation('~'); break;
            case "less": logical(0); break;
            case "more": logical(1); break;
            case "equal": logical(2); break;
            default: break;
        }
    }

    private void operation(char c) {
        if(c == '~') {
            // Unar operation
            Object first = stackValues.get(stackValues.size()-1);
            stackValues.remove(stackValues.size()-1);
            changeSymbol(first);
        } else {
            // Binar operation
            Object first = stackValues.get(stackValues.size()-1);
            stackValues.remove(stackValues.size()-1);
            Object second = stackValues.get(stackValues.size()-1);
            stackValues.remove(stackValues.size()-1);
            castTypes(first, second, c);
        }
        popStack();
    }

    private void changeSymbol(Object first)
    {
        if(first instanceof Float)
        {
            float f = (float) first;
            stackValues.add(-f);
        }
        else if(first instanceof Integer)
        {
            int f = (int) first;
            stackValues.add(-f);
        }
    }

    private void logical(int i) {
        Object first = stackValues.get(stackValues.size()-1);
        stackValues.remove(stackValues.size()-1);
        Object second = stackValues.get(stackValues.size()-1);
        stackValues.remove(stackValues.size()-1);

        if(i == 0)
            castTypes(first, second, '<');
        else
        if(i == 1)
            castTypes(first, second, '>');
        else
            castTypes(first, second, '=');

        popStack();
    }

    private void evaluation() {
        String currentToken = getToken();
        switch (currentToken) {
            case "INTEGERNUM":
            {
                int result = (Integer) getTokenRes();
                stackValues.add(result);
                nextToken();
                popStack();
                break;
            }
            case "FLOATNUM":
            {
                float result = (float) getTokenRes();
                stackValues.add(result);
                nextToken();
                popStack();
                break;
            }
            case "STRING":
            {
                String result = (String) getTokenRes();
                stackValues.add(result);
                nextToken();
                popStack();
                break;
            }
            default: break;
        }
    }

    private void foundNextRule(String currentToken, String topToken) {
        List<Integer> possibleTokenIndexes = foundIndexOfToken(topToken);
        StringBuilder notFoundTokens = new StringBuilder();
        for(int i = 0; i < possibleTokenIndexes.size(); i++)
        {
            Rule tmpRule = rules.get(possibleTokenIndexes.get(i));
            if(tmpRule.getChoiceUnity().contains(currentToken))
            {
                List<String> tokens = tmpRule.getValue();
                popStack();
                for(int j = tokens.size()-1; j > -1; j--)
                {
                    String tmpToken = tokens.get(j);
                    if(!"e".equals(tmpToken))
                        pushParamsToStack(tmpToken);
                }
                return;
            }
            else
            {
                if(i == possibleTokenIndexes.size()-1)
                    for (int j = 0; j < tmpRule.getChoiceUnity().size(); j++) {
                        notFoundTokens.append(tmpRule.getChoiceUnity().get(j)).append(",");
                    }
            }
        }
        throw new IllegalArgumentException("Unexpected token [" + currentToken + "] expected [" + notFoundTokens + "]");
    }

    private List<Integer> foundIndexOfToken(String name)
    {
        List<Integer> possibleIndexes = new ArrayList<>();
        for (int i = 0; i < rules.size(); i++) {
            if(rules.get(i).getName().equals(name))
                possibleIndexes.add(i);
        }
        return possibleIndexes;
    }

    private String topStack() {
        return storeStack.get(storeStack.size()-1);
    }

    private void popStack() { storeStack.remove(storeStack.size()-1); }

    private void pushParamsToStack(String... parameters) {
        Collections.addAll(storeStack, parameters);
    }

    private void nextToken() { position++; }

    private String getToken() {
        return tokenList.get(position).getType();
    }

    private Object getTokenRes() {
        return tokenList.get(position).getValue();
    }

    private void showAllRules() {
        for (Rule rule : rules) {
            System.out.println(String.format("%s > %s | %s", rule.getName(), rule.getValue(), rule.getChoiceUnity()));
        }
    }

    private String getStringRules() {
        String allRulesString = "";
        try {
            allRulesString = new String(Files.readAllBytes(Paths.get("rules.txt")), "UTF-8");
            allRulesString +="\0";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allRulesString;
    }

    private boolean isLetterOrDigitOrSymbol(char currentSymbol) {
        return Character.isLetterOrDigit(currentSymbol)
                || currentSymbol == '(' || currentSymbol == ')'
                || currentSymbol == '+' || currentSymbol == '-'
                || currentSymbol == '*' || currentSymbol == '/'
                || currentSymbol == '=' || currentSymbol == '^'
                || currentSymbol == ',' || currentSymbol == ';'
                || currentSymbol == '<' || currentSymbol == '>'
                || currentSymbol == '~';
    }

    private void loadRules()
    {
        String allRulesString = getStringRules();
        String name = "";
        int length = allRulesString.length();
        int numberOfSymbol = 0;
        boolean isName = true;
        boolean isRule = true;
        StringBuilder buffer = new StringBuilder();
        List<String> values = new ArrayList<>();
        List<String> choiceUnity = new ArrayList<>();
        while(true) {
            if(numberOfSymbol == length)
                break;
            char currentSymbol = allRulesString.charAt(numberOfSymbol);
            if(currentSymbol == ' ') {
                numberOfSymbol++;
                continue;
            }
            if(isLetterOrDigitOrSymbol(currentSymbol)) {
                buffer.append(currentSymbol);
                numberOfSymbol++;
                continue;
            }
            if(isName) {
                if(currentSymbol == '@') {
                    name = buffer.toString();
                    isName = false;
                    numberOfSymbol++;
                    buffer.setLength(0);
                }
            } else {
                if(currentSymbol == '}' && isRule) {
                    values.add(buffer.toString());
                    buffer.setLength(0);
                } else if(currentSymbol == '}') {
                    choiceUnity.add(buffer.toString());
                    buffer.setLength(0);
                } else if(currentSymbol == '|') {
                    isRule = false;
                    buffer.setLength(0);
                } else if(currentSymbol == '\n' || currentSymbol == '\0') {
                    isName = true;
                    isRule = true;
                    rules.add(new Rule(name, values, choiceUnity));
                    name = "";
                    values = new ArrayList<>();
                    choiceUnity = new ArrayList<>();
                }
                numberOfSymbol++;
            }
        }
    }

    private void castString(Object first, Object second, char c) {
        String tmp1 = first.toString();
        String tmp2 = second.toString();
        String tmp3;
        if(c == '+')
            tmp3 = tmp2 + tmp1;
        else throw new IllegalArgumentException("Operation "+c+" is not supported for strings.");
        stackValues.add(tmp3);
    }

    private void castFloatWithFloat(float tmp1, float tmp2, char c) {
        float tmp3 = 0.0f;
        switch (c)
        {
            case '+': tmp3 = tmp2 + tmp1; break;
            case '-': tmp3 = tmp2 - tmp1; break;
            case '*': tmp3 = tmp2 * tmp1; break;
            case '^': tmp3 = (float) Math.pow(tmp2, tmp1); break;
            case '>': tmp3 = tmp2 > tmp1 ? 1 : 0; break;
            case '<': tmp3 = tmp2 < tmp1 ? 1 : 0; break;
            case '=': tmp3 = tmp2 == tmp1 ? 1 : 0; break;
            case '/': {
                if(tmp1 != 0)
                    tmp3 = tmp2 / tmp1;
                else
                    throw new ArithmeticException("Can't divide by zero");
            } break;
            default: break;
        }
        stackValues.add(tmp3);
    }

    private void castFloat(Object first, Object second, char c) {
        float tmp1 = (float) first;
        float tmp2 = (float) second;
        castFloatWithFloat(tmp1, tmp2, c);
    }

    private void castTypes(Object first, Object second, char c) {
        // byte, short, int, long, float, double, char, boolean, String
        // TODO: Implement supporting int
        if(first instanceof String || second instanceof String) {
            castString(first, second, c);
        } else {
            castFloat(first, second, c);
        }
    }

}

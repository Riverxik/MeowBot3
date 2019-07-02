package com.github.riverxik.meowbot.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        }
        while (true)
        {
            if(!parse())
                break;
        }
        System.out.println("******************");
        if(topStack().equals("BOTTOM"))
            if(isDebug)
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

            if (topToken.equals(rules.get(0).getName())) {
                if (currentToken.equals("ENDOFFILE")) {
                    popStack();
                    return false;
                }
            }

            if (topToken.equals("BOTTOM")) {
                if(currentToken.equals(";")) {
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
                case "SYMBOL": // !
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
                    if(!tmpToken.equals("e"))
                        pushParamsToStack(tmpToken);
                }
                return;
            }
            else
            {
                if(i == possibleTokenIndexes.size()-1)
                    for (int j = 0; j < tmpRule.getChoiceUnity().size(); j++) {
                        notFoundTokens.append(tmpRule.getChoiceUnity().get(j).toString()).append(",");
                    }
                //tmpRule.getChoiceUnity().forEach(notFoundTokens::append);
            }
        }
        throw new RuntimeException("Unexpected token " + currentToken + " expected " + notFoundTokens);
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

    private void expression() {
        String currentToken = getToken();
        if(currentToken.equals("STRING"))
        {
            String currentRes = (String) getTokenRes();
            stackValues.add(currentRes);
        }
        else if(currentToken.equals("FLOATNUM"))
        {
            float currentRes =  (float) getTokenRes();
            stackValues.add(currentRes);
        }
        else if(currentToken.equals("INTEGERNUM"))
        {
            int currentRes = (Integer) getTokenRes();
            stackValues.add(currentRes);
        }
        popStack();
        nextToken();
    }

    private String topStack() {
        return storeStack.get(storeStack.size()-1);
    }

    private void popStack() { storeStack.remove(storeStack.size()-1); }

    private void replaceStack(String... parameters) {
        popStack();
        pushParamsToStack(parameters);
    }

    private void pushParamsToStack(String... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            storeStack.add(parameters[i]);
        }
    }

    private void nextToken() { position++; }

    private String getToken() {
        return tokenList.get(position).getType();
    }

    private Object getTokenRes() {
        return tokenList.get(position).getValue();
    }

    private void showAllRules() {
        for (int i = 0; i < rules.size(); i++) {
            System.out.println(String.format("%s > %s | %s", rules.get(i).getName(), rules.get(i).getValue(), rules.get(i).getChoiceUnity()));
        }
    }

    private void loadRules()
    {
        String allRulesString = "";
        try {
            allRulesString = new String(Files.readAllBytes(Paths.get("rules.txt")), "UTF-8");
            allRulesString +="\0";
        } catch (IOException e) {
            e.printStackTrace();
        }

        int length = allRulesString.length();
        int numberOfSymbol = 0;
        StringBuilder buffer = new StringBuilder();
        boolean isName = true;
        boolean isRule = true;
        String name = "";
        List<String> values = new ArrayList<>();
        List<String> choiceUnity = new ArrayList<>();
        while(true)
        {
            if(numberOfSymbol == length)
                break;
            char currentSymbol = allRulesString.charAt(numberOfSymbol);
            if(currentSymbol == ' ')
            {
                numberOfSymbol++;
                continue;
            }
            if(Character.isLetterOrDigit(currentSymbol)
                    || currentSymbol == '(' || currentSymbol == ')'
                    || currentSymbol == '+' || currentSymbol == '-'
                    || currentSymbol == '*' || currentSymbol == '/'
                    || currentSymbol == '=' || currentSymbol == '^'
                    || currentSymbol == ',' || currentSymbol == ';'
                    || currentSymbol == '<' || currentSymbol == '>'
                    || currentSymbol == '~')
            {
                buffer.append(currentSymbol);
                numberOfSymbol++;
                continue;
            }
            if(isName)
            {
                if(currentSymbol == '@')
                {
                    name = buffer.toString();
                    isName = false;
                    numberOfSymbol++;
                    buffer.setLength(0);
                }
            }
            else
            {
                if(currentSymbol == '}' && isRule)
                {
                    values.add(buffer.toString());
                    numberOfSymbol++;
                    buffer.setLength(0);
                }
                else if(currentSymbol == '}')
                {
                    choiceUnity.add(buffer.toString());
                    numberOfSymbol++;
                    buffer.setLength(0);
                }
                else if(currentSymbol == '\n' || currentSymbol == '\0')
                {
                    isName = true;
                    isRule = true;
                    numberOfSymbol++;
                    rules.add(new Rule(name, values, choiceUnity));
                    name = "";
                    values = new ArrayList<>();
                    choiceUnity = new ArrayList<>();
                }
                else if(currentSymbol == '|')
                {
                    isRule = false;
                    numberOfSymbol++;
                    buffer.setLength(0);
                }
                else
                {
                    numberOfSymbol++;
                }
            }
        }
    }

    /*
    * Really sorry for that old method code but i'm so lazy to make it better now :>
    * I should make it better sometimes.
    * It works, so...
    * */
    private void castTypes(Object first, Object second, char c) {
        String firstType = first.getClass().getSimpleName();
        String secondType = second.getClass().getSimpleName();
        // byte, short, int, long, float, double, char, boolean, String
        if(first instanceof String || second instanceof String)
        {
            String tmp1 = first.toString();
            String tmp2 = second.toString();
            String tmp3 = "";
            if(c == '+')
                tmp3 = tmp2 + tmp1;
            else throw new RuntimeException("Operation "+c+" is not supported for strings.");
            stackValues.add(tmp3);
        }
        else
        if(first instanceof Float)
        {
            float tmp1 = (float) first;
            if(second instanceof Float)
            {
                float tmp2 = (float) second;
                float tmp3 = 0.0f;
                switch (c)
                {
                    case '+': tmp3 = tmp2 + tmp1; break;
                    case '-': tmp3 = tmp2 - tmp1; break;
                    case '*': tmp3 = tmp2 * tmp1; break;
                    case '/':
                    {
                        if(tmp1 != 0)
                            tmp3 = tmp2 / tmp1;
                        else
                            throw new RuntimeException("Can't divide by zero");
                    } break;
                    case '^': tmp3 = (float) Math.pow(tmp2, tmp1); break;
                    case '>':
                    {
                        if(tmp2 > tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '<':
                    {
                        if(tmp2 < tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '=':
                    {
                        if(tmp2 == tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                }
                stackValues.add(tmp3);
            }
            else if(second instanceof Integer)
            {
                int tmp2 = (int) second;
                float tmp3 = 0.0f;
                switch (c)
                {
                    case '+': tmp3 = tmp2 + tmp1; break;
                    case '-': tmp3 = tmp2 - tmp1; break;
                    case '*': tmp3 = tmp2 * tmp1; break;
                    case '/':
                    {
                        if(tmp1 != 0)
                            tmp3 = tmp2 / tmp1;
                        else
                            throw new RuntimeException("Can't divide by zero");
                    } break;
                    case '^': tmp3 = (float) Math.pow(tmp2, tmp1); break;
                    case '>':
                    {
                        if(tmp2 > tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '<':
                    {
                        if(tmp2 < tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '=':
                    {
                        if(tmp2 == tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                }
                stackValues.add(tmp3);
            }
        }
        else if(first instanceof Integer)
        {
            int tmp1 = (int) first;
            if(second instanceof Float)
            {
                float tmp2 = (float) second;
                float tmp3 = 0.0f;
                switch (c)
                {
                    case '+': tmp3 = tmp2 + tmp1; break;
                    case '-': tmp3 = tmp2 - tmp1; break;
                    case '*': tmp3 = tmp2 * tmp1; break;
                    case '/':
                    {
                        if(tmp1 != 0)
                            tmp3 = tmp2 / tmp1;
                        else
                            throw new RuntimeException("Can't divide by zero");
                    } break;
                    case '^': tmp3 = (float) Math.pow(tmp2, tmp1); break;
                    case '>':
                    {
                        if(tmp2 > tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '<':
                    {
                        if(tmp2 < tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '=':
                    {
                        if(tmp2 == tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                }
                stackValues.add(tmp3);
            }
            else if(second instanceof Integer)
            {
                int tmp2 = (int) second;
                int tmp3 = 0;
                switch (c)
                {
                    case '+': tmp3 = tmp2 + tmp1; break;
                    case '-': tmp3 = tmp2 - tmp1; break;
                    case '*': tmp3 = tmp2 * tmp1; break;
                    case '/':
                    {
                        if(tmp1 != 0)
                            tmp3 = tmp2 / tmp1;
                        else
                            throw new RuntimeException("Can't divide by zero");
                    } break;
                    case '^': tmp3 = (int) Math.pow(tmp2, tmp1); break;
                    case '>':
                    {
                        if(tmp2 > tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '<':
                    {
                        if(tmp2 < tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                    case '=':
                    {
                        if(tmp2 == tmp1)
                            tmp3 = 1;
                        else
                            tmp3 = 0;
                    } break;
                }
                stackValues.add(tmp3);
            }
        }
    }

}

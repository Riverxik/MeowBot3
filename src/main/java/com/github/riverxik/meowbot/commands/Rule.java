package com.github.riverxik.meowbot.commands;

import java.util.List;

public class Rule {

    private final String name;
    private final List<String> value;
    private final List<String> choiceUnity;

    public Rule(String name, List<String> value, List<String> choiceUnity) {
        this.name = name;
        this.value = value;
        this.choiceUnity = choiceUnity;
    }

    public String getName() {
        return name;
    }

    public List<String> getValue() {
        return value;
    }

    public List<String> getChoiceUnity() {
        return choiceUnity;
    }
}

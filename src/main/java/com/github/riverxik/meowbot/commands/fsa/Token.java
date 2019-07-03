package com.github.riverxik.meowbot.commands.fsa;

public class Token {

    private String type = null;
    private Object value = null;

    public Token(String type) {
        this.type = type;
    }

    public void setStringValue(String value) { this.value = value; }
    public void setFloatValue(float value) { this.value = value; }
    public void setIntValue(int value) { this.value = value; }
    public String getType() {return this.type; }
    public Object getValue() { return this.value; }

    @Override
    public String toString() {
        return String.format("[%s] = (%s)", type, value);
    }
}

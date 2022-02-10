package com.ggs.bot.constants;

public enum CommandType {

    SLASH("slash"), COMMAND("command"), CARD("card"), SMALL_TALK("smalltalk"), HELP("help");
    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

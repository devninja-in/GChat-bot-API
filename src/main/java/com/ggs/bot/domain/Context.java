package com.ggs.bot.domain;

import com.ggs.bot.constants.CommandType;
import com.ggs.bot.constants.EventType;

public abstract class Context {
    private User sender;
    private EventType eventType;
    private CommandType commandType;
    private String spaceName;
    private String spaceType;
    private String messageThreadName;

    public User getSender() {
        return sender;
    }

    public Context setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Context setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Context setCommandType(CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public Context setSpaceName(String spaceName) {
        this.spaceName = spaceName;
        return this;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public Context setSpaceType(String spaceType) {
        this.spaceType = spaceType;
        return this;
    }

    public String getMessageThreadName() {
        return messageThreadName;
    }

    public Context setMessageThreadName(String messageThreadName) {
        this.messageThreadName = messageThreadName;
        return this;
    }
}

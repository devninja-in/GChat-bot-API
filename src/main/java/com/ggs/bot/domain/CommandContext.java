package com.ggs.bot.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.StringJoiner;

public class CommandContext extends Context {

    private String command;
    private String translatedCommand;
    private DetectIntentResponse detectCommandIntent;
    private JsonNode event;

    public String getCommand() {
        return command;
    }

    public CommandContext setCommand(String command) {
        this.command = command;
        return this;
    }

    public String getTranslatedCommand() {
        return StringUtils.isBlank(translatedCommand) ? command : translatedCommand;
    }

    public CommandContext setTranslatedCommand(String translatedCommand) {
        this.translatedCommand = translatedCommand;
        return this;
    }

    public DetectIntentResponse getDetectCommandIntent() {
        return detectCommandIntent;
    }

    public CommandContext setDetectCommandIntent(DetectIntentResponse detectCommandIntent) {
        this.detectCommandIntent = detectCommandIntent;
        return this;
    }

    public JsonNode getEvent() {
        return event;
    }

    public CommandContext setEvent(JsonNode event) {
        this.event = event;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommandContext.class.getSimpleName() + "[", "]")
            .add("command='" + command + "'").add("translatedCommand='" + translatedCommand + "'")
            .add("detectCommandIntent=" + detectCommandIntent)
            .add("event=" + event).toString();
    }
}

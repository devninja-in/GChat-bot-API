package com.ggs.bot.processor.gchat.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.constants.EventType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RemoveFromSpaceEventProcessor extends AbstractEventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveFromSpaceEventProcessor.class);
    @ConfigProperty(name = "bot.name") private String botName;

    @Override
    public Message process(JsonNode event) {
        LOGGER.info("Received event: {}", event.toString());
        String name = event.at("/space/name").asText();
        LOGGER.info("{} removed from {}", botName, name);
        return new Message();
    }

    @Override
    public EventType getEventType() {
        return EventType.REMOVED_FROM_SPACE;
    }
}

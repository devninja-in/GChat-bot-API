package com.ggs.bot.processor.gchat.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggs.bot.constants.EventType;
import com.google.api.services.chat.v1.model.Message;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddToSpaceEventProcessor extends AbstractEventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddToSpaceEventProcessor.class);
    @ConfigProperty(name = "bot.name") private String botName;

    @Override
    public Message process(JsonNode event) {
        LOGGER.info("Received event: {}", event.toString());
        String spaceType = event.at("/space/type").asText();
        if ("ROOM".equals(spaceType)) {
            String displayName = event.at("/space/displayName").asText();
            LOGGER.info("{} added in {} room", botName, displayName);
        }
        else {
            String displayName = event.at("/user/displayName").asText();
            LOGGER.info("{} added by {}", botName, displayName);
        }
        Message reply = new Message();
        reply.setText(String.format("Hello! I'm *%s*, your new assistant.  I can help you", botName));
        return reply;
    }

    @Override
    public EventType getEventType() {
        return EventType.ADDED_TO_SPACE;
    }
}

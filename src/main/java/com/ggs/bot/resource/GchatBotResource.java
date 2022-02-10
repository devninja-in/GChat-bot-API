package com.ggs.bot.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggs.bot.constants.EventType;
import com.ggs.bot.processor.gchat.GChatEventProcessor;
import com.google.api.services.chat.v1.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class GchatBotResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GchatBotResource.class);

    private final Map<EventType, GChatEventProcessor> EVENT_PROCESSORS = new HashMap<>();

    @Inject
    public void setEventProcessors(Instance<GChatEventProcessor> gChatEventProcessors){
       for(GChatEventProcessor gChatEventProcessor : gChatEventProcessors){
           EVENT_PROCESSORS.put(gChatEventProcessor.getEventType(), gChatEventProcessor);
       }
    }

    @POST
    @Path("/gchat/event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Message onEvent(JsonNode event) {

        Message reply = new Message();
        String eventType = event.get("type").asText();
        GChatEventProcessor eventProcessor = EVENT_PROCESSORS.get(EventType.valueOf(eventType));
        if (eventProcessor != null) {
            reply = eventProcessor.process(event);
        }
        else {
            LOGGER.info("No event processor found for {} event type", eventType);
            reply.setText("Cannot determine event type");
        }
        return reply;
    }


    //TODO: add annotation
    public Response handleCustomRuntimeException(Exception exception){
        return Response.serverError()
            .status(Status.INTERNAL_SERVER_ERROR)
            .entity(new Message().setText("Failed to process request"))
            .build();
    }
}
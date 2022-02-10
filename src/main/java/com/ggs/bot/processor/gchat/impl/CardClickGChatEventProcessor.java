package com.ggs.bot.processor.gchat.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggs.bot.constants.CommandType;
import com.ggs.bot.constants.EventType;
import com.ggs.bot.domain.User;
import com.ggs.bot.service.gchat.GChatMessageService;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.ActionContext;
import com.ggs.bot.handler.action.ActionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ApplicationScoped
public class CardClickGChatEventProcessor extends AbstractEventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardClickGChatEventProcessor.class);
    private final Map<String, ActionHandler> actionHandlers = new HashMap<>();

    @Inject
    private GChatMessageService gChatMessageService;

    @Inject
    public void setMessageCommandHandlers(Instance<ActionHandler> actionHandlers) {
        for (ActionHandler actionHandler : actionHandlers) {
            this.actionHandlers.put(actionHandler.getAction().getName(), actionHandler);
        }
    }

    @Override
    public EventType getEventType() {
        return EventType.CARD_CLICKED;
    }

    @Override
    public Message process(JsonNode event) {
        LOGGER.info("Received event: {}", event.toString());
        String actionMethodName = getActionMethodName(event);
        ActionHandler actionHandler = actionHandlers.get(actionMethodName);

        if (actionHandler == null) {
            LOGGER.warn("No action handle found for {} command", actionMethodName);
            return new Message().setText("Invalid action");
        }

        ActionContext actionContext = createActionContext(actionMethodName, actionHandler.getCommandType(),
            event);

        if (isGchatUserIdOrSpaceIdEmpty(actionContext.getSender())) {
            if (isDMSpaceType(event)) {
                updateGchatUserIdAndSpaceId(actionContext.getSender(), getSenderUserId(event),
                    getSpaceName(event));
            }
            else {
                return getGchatUserIdOrSpaceIdEmptyErrorResponse(event, actionContext.getSpaceName(),
                    actionContext.getMessageThreadName(), gChatMessageService);
            }
        }

        Message message = actionHandler.handle(actionContext);
        LOGGER.info("Response: {}", message.toString());
        return message;
    }

    private ActionContext createActionContext(String actionMethodName, CommandType commandType,
        JsonNode event) {
        User sender = getSender(event);
        ActionContext actionContext = new ActionContext();
        actionContext.setActionMethodName(actionMethodName).setCardId(getActionCardId(event))
            .setConceptId(getConceptId(event)).setParameters(getActionParameters(event))
            .setFormInputs(getFormInputs(event)).setSender(sender).setEventType(getEventType())
            .setCommandType(commandType).setSpaceName( getSpaceName(event)).setSpaceType(getSpaceType(event))
            .setMessageThreadName(getMessageThreadName(event));
        return actionContext;
    }

    private JsonNode getFormInputs(JsonNode event) {
        return event.at("/common/formInputs");
    }

    private JsonNode getActionParameters(JsonNode event) {
        return event.at("/action/parameters");
    }

    private String getActionCardId(JsonNode event) {
        JsonNode actionParameters = event.at("/action/parameters");
        Iterator<JsonNode> iterator = actionParameters.iterator();
        while (iterator.hasNext()) {
            JsonNode actionParameter = iterator.next();
            if ("cardId".equalsIgnoreCase(actionParameter.at("/key").asText())) {
                return actionParameter.at("/value").asText();
            }
        }
        return null;
    }

    private String getConceptId(JsonNode event) {
        JsonNode actionParameters = event.at("/action/parameters");
        Iterator<JsonNode> iterator = actionParameters.iterator();
        while (iterator.hasNext()) {
            JsonNode actionParameter = iterator.next();
            if ("conceptId".equalsIgnoreCase(actionParameter.at("/key").asText())) {
                return actionParameter.at("/value").asText();
            }
        }
        return null;
    }

    private String getActionMethodName(JsonNode event) {
        return event.at("/action/actionMethodName").asText();
    }
}

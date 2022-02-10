package com.ggs.bot.handler.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggs.bot.constants.CommandType;
import com.ggs.bot.service.ActivityLogService;
import com.ggs.bot.service.UtilityService;
import com.google.api.services.chat.v1.model.ActionResponse;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.ActionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

public abstract class AbstractActionHandler implements ActionHandler {

    @Inject private ActivityLogService activityLogService;
    @Inject private UtilityService utilityService;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractActionHandler.class);

    @Override
    public boolean logActivity() {
        return false;
    }

    @Override
    public boolean translateCommand() {
        return false;
    }

    @Override
    public boolean translateResponse() {
        return false;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.CARD;
    }

    @Override
    public String getUpdateType() {
        return "update";
    }

    @Override
    public String getConcept() {
        return null;
    }

    @Override
    public Message handle(ActionContext actionContext) {
        Message message = handleRequest(actionContext);

        // Check Language settings
        if (translateResponse() && isNotEnglish(actionContext.getSender().getLanguage())) {
            message = utilityService.translate(message, "en", actionContext.getSender().getLanguage());
        }

        setActionResponseType(message);

        if (logActivity()) {
            activityLogService.log(actionContext);
        }
        return message;
    }



    protected abstract Message handleRequest(ActionContext actionContext);

    private void setActionResponseType(Message message) {
        String type = null;
        if ("update".equalsIgnoreCase(getUpdateType())) {
            type = "UPDATE_MESSAGE";
        }
        else if ("new".equalsIgnoreCase(getUpdateType())) {
            type = "NEW_MESSAGE";
        }
        else if ("dialog".equalsIgnoreCase(getUpdateType())) {
            type = "DIALOG";
        }

        if (type != null) {

            Map actionResponseMap = (Map) message.get("action_response");
            if (actionResponseMap != null) {
                actionResponseMap.put("type", type);
                return;
            }

            ActionResponse actionResponse = message.getActionResponse();
            if (actionResponse == null) {
                actionResponse = new ActionResponse();
                message.setActionResponse(actionResponse);
            }
            actionResponse.setType(type);
        }

    }

    private boolean isNotEnglish(String language) {
        return !"en".equalsIgnoreCase(language);
    }
}

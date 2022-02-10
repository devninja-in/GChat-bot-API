package com.ggs.bot.handler.action;

import com.ggs.bot.constants.Action;
import com.ggs.bot.constants.CommandType;
import com.ggs.bot.constants.EventType;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.ActionContext;

public interface ActionHandler {

    Action getAction();

    CommandType getCommandType();

    default EventType getEventType() {
        return EventType.CARD_CLICKED;
    }

    Message handle(ActionContext actionContext);

    String getIntent();

    boolean logActivity();

    boolean translateCommand();

    boolean translateResponse();


    String getConcept();

    String getUpdateType();

}

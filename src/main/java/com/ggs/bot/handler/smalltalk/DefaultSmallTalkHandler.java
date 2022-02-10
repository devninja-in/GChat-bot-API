package com.ggs.bot.handler.smalltalk;

import com.ggs.bot.constants.CommandType;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.CommandContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultSmallTalkHandler implements SmallTalkHandler {

    @Override
    public Message handle(CommandContext commandContext) {
        return new Message().setText(commandContext.getDetectCommandIntent().getQueryResult().getFulfillmentText());
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.SMALL_TALK;
    }

    @Override
    public String getAction() {
        return null;
    }
}

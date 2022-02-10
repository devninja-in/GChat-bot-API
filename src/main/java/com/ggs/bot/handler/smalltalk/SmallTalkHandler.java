package com.ggs.bot.handler.smalltalk;

import com.ggs.bot.constants.CommandType;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.CommandContext;

public interface SmallTalkHandler {

    Message handle(CommandContext commandContext);

    CommandType getCommandType();

    String getAction();
}

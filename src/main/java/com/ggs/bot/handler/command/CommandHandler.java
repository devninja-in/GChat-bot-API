package com.ggs.bot.handler.command;

import com.ggs.bot.constants.CommandType;
import com.ggs.bot.constants.EventType;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.CommandContext;

public interface CommandHandler {

   default EventType getEventType() {
      return EventType.MESSAGE;
   }

   CommandType getCommandType();
   String getCommandName();

   boolean logActivity();
   boolean translateCommand();
   boolean detectMessageIntent();
   boolean translateResponse();

   Message handle(CommandContext commandContext);


}

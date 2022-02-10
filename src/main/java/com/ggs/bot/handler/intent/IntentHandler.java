package com.ggs.bot.handler.intent;

import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.CommandContext;

public interface IntentHandler {

    Message handle(CommandContext commandContext);

    String getIntentName();
}

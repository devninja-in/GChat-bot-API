package com.ggs.bot.handler.intent;

import com.ggs.bot.domain.CommandContext;
import com.google.api.services.chat.v1.model.Message;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultFallbackIntentHandler implements IntentHandler {

    @Override
    public String getIntentName() {
        return "Default Fallback Intent";
    }

    @Override
    public Message handle(CommandContext commandContext) {

        String senderId = commandContext.getSender().getGchatUserId();
        String formattedSenderId = String.format("<%s>", senderId);
   
        return new Message().setText(
            String.format("Hi %s, This is default Fallback Intent handler Response", formattedSenderId));
    }
}

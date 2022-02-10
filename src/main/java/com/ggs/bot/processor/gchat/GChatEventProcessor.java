package com.ggs.bot.processor.gchat;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.processor.EventProcessor;

public interface GChatEventProcessor extends EventProcessor<JsonNode, Message> {

}

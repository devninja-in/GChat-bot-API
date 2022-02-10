package com.ggs.bot.service.gchat;

import com.google.api.services.chat.v1.model.Message;

import java.io.IOException;

public interface GChatMessageService {

    Message sendMessage(Message message, String Space, String Thread) throws IOException;

}

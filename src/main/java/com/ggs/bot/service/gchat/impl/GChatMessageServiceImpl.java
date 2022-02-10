package com.ggs.bot.service.gchat.impl;

import com.ggs.bot.service.gchat.GChatMessageService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.Message;
import com.google.api.services.chat.v1.model.Thread;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.security.GeneralSecurityException;

@ApplicationScoped
public class GChatMessageServiceImpl implements GChatMessageService {
    static final String CHAT_SCOPE = "https://www.googleapis.com/auth/chat.bot";
    private final HangoutsChat chatClient;

    public GChatMessageServiceImpl() throws GeneralSecurityException, IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault().createScoped(CHAT_SCOPE);
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        chatClient = new HangoutsChat.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                requestInitializer)
                .build();
    }

    @Override
    public Message sendMessage(Message message, String spaceName, String threadName) throws IOException {

        if (StringUtils.isNotBlank(threadName)) {
            Thread thread = new Thread().setName(threadName);
            message.setThread(thread);
        }

        return chatClient.spaces().messages().create(spaceName, message).execute();
    }
}

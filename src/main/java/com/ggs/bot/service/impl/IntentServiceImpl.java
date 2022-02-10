package com.ggs.bot.service.impl;

import com.ggs.bot.service.IntentService;
import com.google.cloud.dialogflow.v2.*;
import com.google.cloud.dialogflow.v2.TextInput.Builder;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

@ApplicationScoped
public class IntentServiceImpl implements IntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntentServiceImpl.class);

    @ConfigProperty(name = "bot.project.id")
    private String projectId;
    @ConfigProperty(name = "bot.session.id")
    private String sessionId;
    @ConfigProperty(name = "bot.language.code")
    private String languageCode;

    @Override
    public DetectIntentResponse detect(String text) {

        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
            SessionName session = SessionName.of(projectId, sessionId);
            LOGGER.info("Session Path %s", session.toString());

            // Detect intents for each text input

            // Set the text (hello) and language code (en-US) for the query
            Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

            // Build the query with the TextInput
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            // Performs the detect intent request

            return sessionsClient.detectIntent(session, queryInput);

        }
        catch (IOException e) {
            //TODO:
            throw new RuntimeException(e);
        }
    }

}

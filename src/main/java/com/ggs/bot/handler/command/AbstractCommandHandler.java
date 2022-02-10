package com.ggs.bot.handler.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggs.bot.service.ActivityLogService;
import com.ggs.bot.service.IntentService;
import com.ggs.bot.service.LanguageService;
import com.ggs.bot.service.UtilityService;
import com.google.api.services.chat.v1.model.Message;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.ggs.bot.domain.CommandContext;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractCommandHandler implements CommandHandler {

    @Inject private LanguageService languageService;
    @Inject private IntentService intentService;
    @Inject private ActivityLogService activityLogService;
    @Inject private UtilityService utilityService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Message handle(CommandContext commandContext) {
        if (translateCommand() && isNotEnglish(commandContext.getSender().getLanguage())) {
            String translatedCommand = languageService
                .translate(commandContext.getCommand(), commandContext.getSender().getLanguage(), "en");
            commandContext.setTranslatedCommand(translatedCommand);
        } else {
            commandContext.setTranslatedCommand(commandContext.getCommand());
        }

        if (detectMessageIntent()) {
            DetectIntentResponse detectIntentResponse = intentService
                .detect(commandContext.getTranslatedCommand());
            commandContext.setDetectCommandIntent(detectIntentResponse);
        }

        Message message = handleRequest(commandContext);

        if (translateResponse() && !"en".equalsIgnoreCase(commandContext.getSender().getLanguage())) {
            message = utilityService.translate(message, "en", commandContext.getSender().getLanguage());
        }

        if (logActivity()) {
            activityLogService.log(commandContext);
        }
        return message;
    }

    protected Message getCommandResponseTemplate(String fileName) {
        InputStream inputStream = AbstractCommandHandler.class
            .getResourceAsStream("/command/response/template/" + fileName);
        try {
            return mapper.readValue(inputStream, Message.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load command response");
        }
    }

    protected abstract Message handleRequest(CommandContext commandContext);

    private boolean isNotEnglish(String language) {
        return  !"en".equalsIgnoreCase(language);
    }

}

package com.ggs.bot.handler.command.message;

import com.ggs.bot.constants.CommandType;
import com.ggs.bot.handler.command.AbstractCommandHandler;
import com.google.api.services.chat.v1.model.Message;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.ggs.bot.domain.CommandContext;
import com.ggs.bot.handler.intent.IntentHandler;
import com.ggs.bot.handler.smalltalk.SmallTalkHandler;

import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class DefaultMessageCommandHandler extends AbstractCommandHandler {

    private final Map<String, IntentHandler> intentHandlers = new HashMap<>();
    @Inject private SmallTalkHandler smallTalkHandler;

    @Inject
    public void setCommandHandlers(Instance<IntentHandler> intentHandlers) {
        for (IntentHandler intentHandler : intentHandlers) {
            this.intentHandlers.put(intentHandler.getIntentName(), intentHandler);
        }
    }

    @Override
    protected Message handleRequest(CommandContext commandContext) {
        DetectIntentResponse detectIntentResponse = commandContext.getDetectCommandIntent();
        if(detectIntentResponse == null){
            return new Message().setText("Invalid intent message");
        }
        IntentHandler intentHandler = intentHandlers
            .get(detectIntentResponse.getQueryResult().getIntent().getDisplayName());
        if (intentHandler != null) {
            return intentHandler.handle(commandContext);
        }
        else if (StringUtils.isNotBlank(detectIntentResponse.getQueryResult().getAction()) &&
            detectIntentResponse.getQueryResult().getAction().startsWith("smalltalk")) {
            return smallTalkHandler.handle(commandContext);
        }
        else {
            //TODO: what should be correct response here
            return new Message().setText("Invalid intent message");
        }
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.COMMAND;
    }

    @Override
    public String getCommandName() {
        return "default";
    }

    @Override
    public boolean logActivity() {
        return false;
    }

    @Override
    public boolean translateCommand() {
        return false;
    }

    @Override
    public boolean detectMessageIntent() {
        return false;
    }

    @Override
    public boolean translateResponse() {
        return false;
    }

}

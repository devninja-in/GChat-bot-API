package com.ggs.bot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.ggs.bot.constants.CommandType;
import com.ggs.bot.domain.ActionContext;
import com.ggs.bot.domain.CommandContext;
import com.ggs.bot.service.ActivityLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ActivityLogServiceImpl implements ActivityLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final String datasetName = "bot_usage_log";
    private final String tableName = "activity";

    @Override
    public void log(CommandContext commandContext) {

        try {
            Map<String, Object> rowContent = new HashMap<>();
            rowContent.put("title", "activity_log");
            rowContent.put("eventDate", new Date());
            rowContent.put("commandType", commandContext.getCommandType());
            rowContent.put("displayName", commandContext.getSender().getDisplayName());
            rowContent.put("email", commandContext.getSender().getEmail());
            rowContent.put("spaceName", commandContext.getSpaceName());
            rowContent.put("messageThreadName", commandContext.getMessageThreadName());
            rowContent.put("name", commandContext.getSender().getDisplayName());

            // COMMAND BASED LOGGING
            if (commandContext.getCommandType().equals(CommandType.COMMAND)) {
                QueryResult queryResult = commandContext.getDetectCommandIntent().getQueryResult();
                rowContent.put("command_messageText", commandContext.getCommand());

                rowContent.put("command_intentName", queryResult.getIntent().getDisplayName());
                Boolean matchFound = queryResult.getFulfillmentText().equalsIgnoreCase("Match found");
                rowContent.put("command_intentDetected", matchFound);
                rowContent.put("command_confidence", queryResult.getIntentDetectionConfidence());
                try {
                    if (queryResult.getParameters().getFieldsOrThrow("concept").getListValue().getValuesList()
                        .size() > 0) {
                        rowContent.put("command_concept",
                            queryResult.getParameters().getFieldsOrThrow("concept").getListValue()
                                .getValues(0).getStringValue());
                    }
                    else {
                        rowContent.put("command_concept", "no_concept");
                    }
                }
                catch (Exception e) {
                    LOGGER.error("Failed to log activity", e);
                }
            }

            // SLASH COMMAND BASED LOGGING
            if (commandContext.getCommandType().equals(CommandType.SLASH)) {
                rowContent.put("slash_text", commandContext.getCommand());
                //                TODO: Implement ability to log both the slash command and its arguments
            }
            System.out.println(mapper.writeValueAsString(rowContent));
        }
        catch (Exception e) {
            LOGGER.error("Failed to log activity", e);
        }
    }

    @Override
    public void log(ActionContext actionContext) {
        try {
            Map<String, Object> rowContent = new HashMap<>();
            rowContent.put("title", "activity_log");
            rowContent.put("eventDate", new Date());
            rowContent.put("commandType", actionContext.getCommandType());
            rowContent.put("displayName", actionContext.getSender().getDisplayName());
            rowContent.put("email", actionContext.getSender().getEmail());
            rowContent.put("spaceName", actionContext.getSpaceName());
            rowContent.put("messageThreadName", actionContext.getMessageThreadName());
            rowContent.put("name", actionContext.getSender().getDisplayName());

            // CARD BASED LOGGING
            if (actionContext.getCommandType().equals(CommandType.CARD)) {
                rowContent.put("card_id", actionContext.getCardId());
            }

            System.out.println(mapper.writeValueAsString(rowContent));
        }
        catch (Exception e) {
            LOGGER.error("Failed to log activity", e);
        }
    }
}


package com.ggs.bot.processor.gchat.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggs.bot.domain.User;
import com.ggs.bot.processor.gchat.GChatEventProcessor;
import com.ggs.bot.service.UserService;
import com.google.api.services.chat.v1.model.ActionResponse;
import com.google.api.services.chat.v1.model.ActionStatus;
import com.google.api.services.chat.v1.model.DialogAction;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.service.gchat.GChatMessageService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public abstract class AbstractEventProcessor implements GChatEventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventProcessor.class);
    @Inject private UserService userService;

    protected String getMessageThreadName(JsonNode event) {
        return event.at("/message/thread/name").asText();
    }

    protected String getSpaceName(JsonNode event) {
        return event.at("/space/name").asText();
    }

    protected String getSpaceType(JsonNode event) {
        return event.at("/space/type").asText();
    }

    protected String getCommand(JsonNode event) {
        String command = event.at("/message/argumentText").asText();
        return StringUtils.isNotBlank(command) ? command.trim() : command;
    }

    protected String getCommandId(JsonNode event) {
        String commandId = event.at("/message/slashCommand/commandId").asText();
        return StringUtils.isNotBlank(commandId) ? commandId.trim() : commandId;
    }

    protected String getSenderDisplayName(JsonNode event) {
        return event.at("/user/displayName").asText();
    }

    protected String getSenderEmail(JsonNode event) {
        return event.at("/user/email").asText();
    }

    protected String getSenderUserId(JsonNode event) {
        return event.at("/user/name").asText();
    }

    protected boolean isDMSpaceType(JsonNode event) {
        return "DM".equalsIgnoreCase(getSpaceType(event));
    }

    protected boolean isDialogEvent(JsonNode event) {
        return event.has("isDialogEvent") ? event.at("/isDialogEvent").asBoolean() : false;
    }

    protected User getSender(JsonNode event) {
        String displayName = getSenderDisplayName(event);
        String senderEmail = getSenderEmail(event);
        User user;
        try {
            user = userService.getUserByEmail(senderEmail);
            if (user == null) {
                user = userService.createUserWithDefaultValues(displayName, senderEmail);
            }
        }
        catch (Exception e) {
            LOGGER.warn(
                "Exception occurred while fetching user by {} email, hence populating user from event message",
                senderEmail);
            user = userService.getUserWithDefaultValues(displayName, senderEmail);
        }
        return user;
    }

    protected boolean isGchatUserIdOrSpaceIdEmpty(User user) {
        return StringUtils.isBlank(user.getGchatUserId()) || StringUtils.isBlank(user.getGchatSpaceId());
    }

    protected void updateGchatUserIdAndSpaceId(User user, String gchatUserId, String gchatSpaceId) {
        user.setGchatUserId(gchatUserId);
        user.setGchatSpaceId(gchatSpaceId);
        userService.updateUser(user);
        LOGGER.info("Updated gchat space id {} for {} user", gchatUserId, user.getEmail());
    }

    protected Message getGchatUserIdOrSpaceIdEmptyErrorResponse(JsonNode event, String spaceName,
        String threadName, GChatMessageService gChatMessageService) {
        Message message = new Message().setText(
            String.format("<%s> To continue using me, please send me a message in a direct message (DM).",
                getSenderUserId(event)));
        if (isDialogEvent(event)) {
            try {
                gChatMessageService.sendMessage(message, spaceName, threadName);
            }
            catch (Exception exception) {
                LOGGER.error("Failed to send space id or user id empty response", exception);
            }
            return new Message().setActionResponse(new ActionResponse().setType("DIALOG")
                .setDialogAction(new DialogAction().setActionStatus(new ActionStatus().setStatusCode("OK"))));
        }
        else {
            return message;
        }
    }

}

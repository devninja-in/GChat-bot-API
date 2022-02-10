package com.ggs.bot.processor.gchat.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggs.bot.constants.CommandType;
import com.ggs.bot.constants.EventType;
import com.ggs.bot.domain.User;
import com.ggs.bot.handler.command.CommandHandler;
import com.ggs.bot.handler.command.slash.AbstractSlashCommandHandler;
import com.ggs.bot.service.gchat.GChatMessageService;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.CommandContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class MessageGChatEventProcessor extends AbstractEventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageGChatEventProcessor.class);

    private final Map<String, CommandHandler> slashCommandHandlers = new HashMap<>();
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();
    private CommandHandler defaultCommandHandler;

    @Inject private GChatMessageService gChatMessageService;
    @ConfigProperty(name = "bot.name") private String botName;

    @Inject
    public void setMessageCommandHandlers(Instance<CommandHandler> commandHandlers) {
        for (CommandHandler commandHandler : commandHandlers) {

            if (EventType.MESSAGE.equals(commandHandler.getEventType())) {
                if (CommandType.COMMAND.equals(commandHandler.getCommandType())) {
                    defaultCommandHandler = commandHandler;
                }
                else if (CommandType.SLASH.equals(commandHandler.getCommandType())) {
                    AbstractSlashCommandHandler slashCommandHandler = (AbstractSlashCommandHandler) commandHandler;
                    slashCommandHandlers.put(slashCommandHandler.getCommandId(), slashCommandHandler);
                }
                else {
                    this.commandHandlers.put(commandHandler.getCommandName(), commandHandler);
                }
            }
        }
    }

    @Override
    public EventType getEventType() {
        return EventType.MESSAGE;
    }

    @Override
    public Message process(JsonNode event) {

        LOGGER.info("Received event: {}", event.toString());
        CommandHandler commandHandler;

        String commandId = getCommandId(event);
        String command = getCommand(event);
        LOGGER.info("Finding command handler for '{}' command ID or '{}' command", commandId, command);
        if (StringUtils.isNotBlank(commandId)) {
            commandHandler = slashCommandHandlers.get(commandId);
            command = commandHandler.getCommandName();
        }
        else {
            commandHandler = commandHandlers.get(command);

            if (commandHandler == null) {
                commandHandler = defaultCommandHandler;
            }

        }

        if (commandHandler == null) {
            LOGGER.warn("No command handle found for {} command Id or {} command", commandId, command);
            return new Message().setText("Invalid command");
        }

        CommandContext commandContext = createCommandContext(command, commandHandler.getCommandType(), event);

        if (isGchatUserIdOrSpaceIdEmpty(commandContext.getSender())) {
            if (isDMSpaceType(event)) {
                updateGchatUserIdAndSpaceId(commandContext.getSender(), getSenderUserId(event),
                    getSpaceName(event));
            }
            else {
                return getGchatUserIdOrSpaceIdEmptyErrorResponse(event, commandContext.getSpaceName(),
                    commandContext.getMessageThreadName(), gChatMessageService);
            }
        }

        LOGGER.info("Calling {} command handler", commandHandler.getCommandName());
        Message message = commandHandler.handle(commandContext);
        LOGGER.info("Response: {}", message.toString());
        return message;
    }

    protected CommandContext createCommandContext(String command, CommandType commandType, JsonNode event) {
        User sender = getSender(event);
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(command).setEvent(event).setSender(sender).setEventType(getEventType())
            .setCommandType(commandType).setSpaceName(getSpaceName(event)).setSpaceType(getSpaceType(event))
            .setMessageThreadName(getMessageThreadName(event));
        return commandContext;
    }
}

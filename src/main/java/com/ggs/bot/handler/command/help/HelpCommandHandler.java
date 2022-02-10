package com.ggs.bot.handler.command.help;

import com.ggs.bot.constants.CommandType;
import com.ggs.bot.constants.EventType;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.domain.CommandContext;
import com.ggs.bot.handler.command.AbstractCommandHandler;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelpCommandHandler extends AbstractCommandHandler {

    private static final String COMMAND_NAME = "help";

    @Override
    public EventType getEventType() {
        return EventType.MESSAGE;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.HELP;
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

    @Override
    protected Message handleRequest(CommandContext commandContext) {
        return getCommandResponseTemplate("helpResponseTemplate.json");
    }
}

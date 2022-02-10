package com.ggs.bot.handler.command.slash;

import com.ggs.bot.constants.CommandType;
import com.ggs.bot.handler.command.AbstractCommandHandler;

public abstract class AbstractSlashCommandHandler extends AbstractCommandHandler {

    @Override
    public CommandType getCommandType(){
        return CommandType.SLASH;
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


    public abstract String getCommandId();
}

package com.ggs.bot.service;

import com.ggs.bot.domain.ActionContext;
import com.ggs.bot.domain.CommandContext;

public interface ActivityLogService {

    void log(CommandContext commandContext);
    void log(ActionContext actionContext);
}

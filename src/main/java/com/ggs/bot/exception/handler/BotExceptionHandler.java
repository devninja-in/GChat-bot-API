package com.ggs.bot.exception.handler;

import com.ggs.bot.exception.BotException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BotExceptionHandler implements ExceptionMapper<BotException> {
    @Override
    public Response toResponse(BotException e) {
        return Response.status(e.getCode()).entity(e.getMessage()).build();
    }
}

package com.ggs.bot.exception;

import java.io.Serializable;

public class BotException extends RuntimeException implements Serializable
{
    Integer code;

    public Integer getCode() {
        return code;
    }

    public BotException setCode(Integer code) {
        this.code = code;
        return this;
    }

    private static final long serialVersionUID = 1L;

    public BotException() {
        super();
    }
    public BotException(Integer code,String msg)   {
        super(msg);
        this.code=code;
    }
    public BotException(String msg, Exception e)  {
        super(msg, e);
    }
}
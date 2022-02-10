package com.ggs.bot.processor;

import com.ggs.bot.constants.EventType;

public interface EventProcessor<T, R> {

    EventType getEventType();

    R process(T event);

}

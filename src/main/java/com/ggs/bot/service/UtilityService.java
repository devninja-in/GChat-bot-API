package com.ggs.bot.service;

import com.google.api.services.chat.v1.model.Message;

import java.util.Date;
import java.util.Map;

public interface UtilityService {

    Message translate(Message message, String sourceLanguage, String destinationLanguage);

    Message getCommandResponseTemplate(String fileName);

    Map<String, Date> calculateTimeframe(String timeframe);

    boolean isPast(String timeframeStr);

    String formatDate(Date date, String format);
}

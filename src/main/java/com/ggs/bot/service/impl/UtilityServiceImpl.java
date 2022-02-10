package com.ggs.bot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.ggs.bot.service.LanguageService;
import com.google.api.services.chat.v1.model.Message;
import com.ggs.bot.handler.command.AbstractCommandHandler;
import com.ggs.bot.service.UtilityService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UtilityServiceImpl implements UtilityService {

    @Inject private LanguageService languageService;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilityServiceImpl.class);

    private static final List<String> MESSAGE_TRANSLATION_KEYS = Arrays.asList("text", "title", "subtitle");

    @Override
    public Message translate(Message message, String sourceLanguage, String destinationLanguage) {

        try {
            String messageStr = mapper.writer().writeValueAsString(message);
            JsonNode jsonNode = mapper.readTree(messageStr);
            translateValue(jsonNode, "en", destinationLanguage);
            message = mapper.readValue(jsonNode.toString(), Message.class);
        }
        catch (Exception e) {
            LOGGER.warn("Failed to translate message to {} language", destinationLanguage, e);
        }

        return message;
    }

    @Override
    public Message getCommandResponseTemplate(String fileName) {
        InputStream inputStream = AbstractCommandHandler.class
                .getResourceAsStream("/command/response/template/" + fileName);
        try {
            return mapper.readValue(inputStream, Message.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load command response");
        }
    }

    @Override
    public Map<String, Date> calculateTimeframe(String timeframe) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        switch (timeframe) {
            case "past": {
                start = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
                end = LocalDateTime.now();
                break;
            }
            case "last months": {
                YearMonth currentMonth = YearMonth.now();
                YearMonth previousMonth = currentMonth.minusMonths(1);
                start = previousMonth.atDay(1).atStartOfDay();
                end = previousMonth.atEndOfMonth().atStartOfDay();
                break;
            }
            case "last years": {
                Year currentYear = Year.now();
                Year previousYear = currentYear.minusYears(1);
                start = previousYear.atDay(1).atStartOfDay();
                end = previousYear.atDay(365).atStartOfDay();
                break;
            }
            case "this months": {
                YearMonth currentMonth = YearMonth.now();
                start = currentMonth.atDay(1).atStartOfDay();
                end = currentMonth.atEndOfMonth().atStartOfDay();
                break;
            }
            case "next months": {
                YearMonth currentMonth = YearMonth.now();
                YearMonth nextMonth = currentMonth.plusMonths(1);
                start = nextMonth.atDay(1).atStartOfDay();
                end = nextMonth.atEndOfMonth().atStartOfDay();
                break;
            }
            case "upcoming":
            case "future": {
                start = LocalDateTime.now();
                end = start.plusYears(2);
                break;
            }
        }

        Map<String, Date> returnMap = new HashMap<>();
        if (start != null) {
            returnMap.put("start", Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));
        }

        if (end != null) {
            returnMap.put("end", Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));
        }

        return returnMap;
    }

    @Override
    public boolean isPast(String timeframe) {
        switch (timeframe) {
            case "last months":
            case "last years":
            case "past": {
                return true;
            }
        }
        return false;
    }

    @Override
    public String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    private void translateValue(JsonNode jsonNode, String sourceLanguage, String targetLanguage) {

        Iterator<JsonNode> iterator = null;
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            objectNode.fieldNames().forEachRemaining(fieldName -> {
                JsonNode valueNode = objectNode.get(fieldName);
                if (MESSAGE_TRANSLATION_KEYS.contains(fieldName) && !valueNode.isNull() &&
                        valueNode.isTextual()) {
                    String translate = languageService
                            .translate(valueNode.asText(), sourceLanguage, targetLanguage);
                    objectNode.set(fieldName, TextNode.valueOf(translate));
                }
            });
            iterator = objectNode.iterator();
        }

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            iterator = arrayNode.iterator();
        }

        if (iterator != null) {
            while (iterator.hasNext()) {
                JsonNode childNode = iterator.next();
                if (!childNode.isTextual()) {
                    translateValue(childNode, sourceLanguage, targetLanguage);
                }
            }
        }
    }

    private String getValue(String value) {
        return StringUtils.isBlank(value) ? "None" : value;
    }

}

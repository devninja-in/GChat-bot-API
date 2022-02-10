package com.ggs.bot.service.pubsub.publisher;

import com.fasterxml.jackson.databind.JsonNode;

public interface PubsubMessagePublishService {
    void publishMessage(String projectId, String topicId, JsonNode message);
}

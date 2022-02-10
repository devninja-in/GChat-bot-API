package com.ggs.bot.service.pubsub.publisher.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggs.bot.service.pubsub.publisher.PubsubMessagePublishService;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class PubsubMessagePublishServiceImpl implements PubsubMessagePublishService {
    private static final Logger LOG = LoggerFactory.getLogger(PubsubMessagePublishServiceImpl.class);

    @Inject
    CredentialsProvider credentialsProvider;

    public void publishMessage(String projectId, String topicId, JsonNode message){
        Publisher publisher = null;

        try {
            // Init topic
            TopicName topicName = TopicName.of(projectId, topicId);

            //Init a publisher to the topic
            publisher = Publisher.newBuilder(topicName).setCredentialsProvider(credentialsProvider).build();

            ByteString data = ByteString.copyFromUtf8(message.toString());

            // Create a new message
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            LOG.info("publishing message to topic {} in project {}", topicId, projectId);
            // Publish the message
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

            // Wait for message submission and log the result
            ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<>() {
                public void onSuccess(String messageId) {
                    LOG.debug("published with message id {}", messageId);
                }

                public void onFailure(Throwable t) {
                    LOG.warn("failed to publish: {0}", t);
                }
            }, MoreExecutors.directExecutor());
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if(publisher != null) {
                try {
                    publisher.shutdown();
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {}
            }
        }
    }
}

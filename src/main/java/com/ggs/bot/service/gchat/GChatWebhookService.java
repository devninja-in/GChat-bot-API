package com.ggs.bot.service.gchat;

import com.google.api.services.chat.v1.model.Message;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@RegisterRestClient
public interface GChatWebhookService {

    @POST
    @Path("{space}/messages")
    String sendMessage(Message message, @PathParam("space") String space, @QueryParam("key") String key, @QueryParam("token") String token);
}
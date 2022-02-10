package com.ggs.bot.service;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface IntentService {

    DetectIntentResponse detect(String text);

}

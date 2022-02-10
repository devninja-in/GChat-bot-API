package com.ggs.bot.service.impl;

import com.ggs.bot.service.LanguageService;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LanguageServiceImpl implements LanguageService {

    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        if(sourceLanguage.equalsIgnoreCase(targetLanguage)){
            return text;
        }
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation = translate.translate(text, TranslateOption.sourceLanguage(sourceLanguage),
            TranslateOption.targetLanguage(targetLanguage));
        return translation.getTranslatedText();
    }
}

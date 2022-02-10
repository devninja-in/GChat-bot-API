package com.ggs.bot.domain;

import com.fasterxml.jackson.databind.JsonNode;

public class ActionContext extends Context{

    private String actionMethodName;
    private String cardId;
    private String conceptId;
    private JsonNode formInputs;
    private JsonNode parameters;

    public String getActionMethodName() {
        return actionMethodName;
    }

    public ActionContext setActionMethodName(String actionMethodName) {
        this.actionMethodName = actionMethodName;
        return this;
    }

    public String getCardId() {
        return cardId;
    }

    public ActionContext setCardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    public ActionContext setConceptId(String conceptId) {
        this.conceptId = conceptId;
        return this;
    }

    public String getConceptId() {
        return conceptId;
    }

    public JsonNode getParameters() {
        return parameters;
    }

    public ActionContext setParameters(JsonNode parameters) {
        this.parameters = parameters;
        return this;
    }

    public JsonNode getFormInputs() {
        return formInputs;
    }

    public ActionContext setFormInputs(JsonNode formInputs) {
        this.formInputs = formInputs;
        return this;
    }
}

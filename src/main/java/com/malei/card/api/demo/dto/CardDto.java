package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

public class CardDto extends ResourceSupport {
    @JsonProperty("id")
    private Long cardId;
    @JsonProperty("name")
    private String cardName;
    @JsonProperty("limit")
    private BigDecimal cardLimitCard;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public BigDecimal getCardLimitCard() {
        return cardLimitCard;
    }

    public void setCardLimitCard(BigDecimal cardLimitCard) {
        this.cardLimitCard = cardLimitCard;
    }
}

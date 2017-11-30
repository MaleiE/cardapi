package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "cards")
public class CardDto extends ResourceSupport {
    @JsonProperty("id")
    private Long cardId;
    @JsonProperty("name")
    private String cardName;
    @JsonProperty("limit")
    private BigDecimal cardLimitCard;
}

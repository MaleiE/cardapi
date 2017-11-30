package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateAndUpdateCardDto extends ResourceSupport {
    @JsonProperty("name")
    private String cardName;
    @JsonProperty("limit")
    private BigDecimal cardLimitCard;
}

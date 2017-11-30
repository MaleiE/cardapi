package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateAndUpdatePurchaseDto extends ResourceSupport {
    @JsonProperty("name")
    private String purchaseName;
    @JsonProperty("price")
    private BigDecimal purchasePrice;
    @JsonProperty("date_purchase")
    private LocalDate purchaseDatePurchase;
    @JsonProperty("months_in_installments")
    private Integer purchaseMonthsInInstallments;
}

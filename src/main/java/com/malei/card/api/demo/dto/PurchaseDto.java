package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "purchase")
public class PurchaseDto extends ResourceSupport {
    @JsonProperty("id")
    private Long purchaseId;
    @JsonProperty("name")
    private String purchaseName;
    @JsonProperty("price")
    private BigDecimal purchasePrice;
    @JsonProperty("months_in_installments")
    private Integer purchaseMonthsInInstallments;
    @JsonProperty("date_purchase")
    private LocalDate purchaseDatePurchase;
    @JsonProperty("date_of_last_payment")
    private LocalDate purchaseDateOfLastPayment;
}

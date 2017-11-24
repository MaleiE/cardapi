package com.malei.card.api.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PurchaseDto {
    private String cardId;
    private String userId;
    private String name;
    private BigDecimal price;
    private Integer monthsInInstallments;
    private LocalDate datePurchase;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMonthsInInstallments() {
        return monthsInInstallments;
    }

    public void setMonthsInInstallments(Integer monthsInInstallments) {
        this.monthsInInstallments = monthsInInstallments;
    }

    public LocalDate getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(LocalDate datePurchase) {
        this.datePurchase = datePurchase;
    }
}

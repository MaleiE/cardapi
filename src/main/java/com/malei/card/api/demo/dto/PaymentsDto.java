package com.malei.card.api.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PaymentsDto {
    private LocalDate datePayment;
    private BigDecimal pay = new BigDecimal(0);
}

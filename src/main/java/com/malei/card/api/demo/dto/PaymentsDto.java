package com.malei.card.api.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Relation(collectionRelation = "payments")
public class PaymentsDto extends ResourceSupport {
    private LocalDate datePayment;
    private BigDecimal pay = new BigDecimal(0);
}

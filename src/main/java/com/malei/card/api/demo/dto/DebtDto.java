package com.malei.card.api.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class DebtDto extends ResourceSupport {
    BigDecimal debt;
}

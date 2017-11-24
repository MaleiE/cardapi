package com.malei.card.api.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "purchase_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price", scale = 2, precision = 9)
    private BigDecimal price;
    @Column(name = "months_in_installments")
    private Integer monthsInInstallments;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_purchase")
    private LocalDate datePurchase;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_of_last_payment")
    private LocalDate dateOfLastPayment;
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card cards;

}

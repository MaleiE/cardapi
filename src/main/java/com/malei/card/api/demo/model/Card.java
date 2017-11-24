package com.malei.card.api.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "card")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id")
    private Long id;
    @Column(name = "card_name")
    String name;
    @Column(name = "limit_card", scale = 2, precision = 9)
    private BigDecimal limitCard;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cards")
    private Set<Purchase> purchases;


}

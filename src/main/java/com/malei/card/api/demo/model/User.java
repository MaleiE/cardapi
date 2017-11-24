package com.malei.card.api.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String name;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Set<Card> cards;

}

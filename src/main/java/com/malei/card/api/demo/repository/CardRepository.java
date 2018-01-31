package com.malei.card.api.demo.repository;


import com.malei.card.api.demo.model.Card;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    //@Query("SELECT card FROM Card WHERE Card.id>10 ")
    //  List<Card> findAll(Pageable pageable);

    List<Card> findAllByUsersId(Sort sort, Long userId);
}

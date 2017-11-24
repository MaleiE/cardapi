package com.malei.card.api.demo.repository;

import com.malei.card.api.demo.model.Purchase;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

/*
    List<Purchase> findAllByCardsIdAndCardsUsersId(Sort sort, Long cardId, Long usersId);
*/
    List<Purchase> findAllByCards_Id(Sort sort, Long cardId);

    List<Purchase> findAllByCardsIdAndDateOfLastPaymentIsBefore(Sort sort, Long cardId, LocalDate today);

    List<Purchase> findAllByCards_UsersId(Sort sort, Long UserId);

    List<Purchase> findAllByCards_UsersIdAndDateOfLastPaymentIsBefore( Sort sort, Long userID, LocalDate today);

    List<Purchase> findAllByCardsIdAndCardsUsersId(/*Sort sort,*/ Long cardId, Long userId);

/*
    List<Purchase> findAllByCardsIdAndCardsUsersIdAndDateOfLastPaymentIsBefore(Sort sort, Long usersId, LocalDate today);
*/

}

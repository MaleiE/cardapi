package com.malei.card.api.demo.repository;

import com.malei.card.api.demo.dto.CardIdUserIdDto;
import com.malei.card.api.demo.model.Purchase;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

/*
    List<Purchase> findAllByCardsIdAndCardsUsersId(Sort sort, Long cardId, Long usersId);
*/
    List<Purchase> findAllByCards_Id(Sort sort, Long cardId);

    List<Purchase> findAllByCardsIdAndDateOfLastPaymentIsBefore(Sort sort, Long cardId, LocalDate today);

    List<Purchase> findAllByCards_UsersId(Sort sort, Long UserId);

    List<Purchase> findAllByCards_UsersIdAndDateOfLastPaymentIsBefore( Sort sort, Long userID, LocalDate today);

    List<Purchase> findAllByCardsIdAndCardsUsersId(/*Sort sort,*/ Long cardId, Long userId);

    @Query(value = "select new com.malei.card.api.demo.dto.CardIdUserIdDto(p.cards.id, c.users.id) from Purchase p inner join p.cards c on c.id = p.cards.id where p.id = :purchaseId"
            /*"SELECT purchase.*, user.user_id\n" +
            "FROM purchase \n" +
            "INNER JOIN card \n" +
            "ON card.card_id = purchase.card_id\n" +
            "INNER JOIN user\n" +
            "ON user.user_id = card.user_id\n" +
            "WHERE purchase.purchase_id = 3", nativeQuery = true*/)
    Optional<CardIdUserIdDto> getPurchaseCardIdAndUserId(@Param("purchaseId") Long purchaseId);

/*
    List<Purchase> findAllByCardsIdAndCardsUsersIdAndDateOfLastPaymentIsBefore(Sort sort, Long usersId, LocalDate today);
*/

}

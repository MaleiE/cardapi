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

    List<Purchase> findAllByCards_Id(Sort sort, Long cardId);

    List<Purchase> findAllByCardsIdAndDateOfLastPaymentIsBefore(Sort sort, Long cardId, LocalDate today);

    List<Purchase> findAllByCards_UsersId(Long UserId);

    List<Purchase> findAllByCards_UsersId(Sort sort, Long UserId);

    List<Purchase> findAllByCards_UsersIdAndDateOfLastPaymentIsBefore( Sort sort, Long userID, LocalDate date);

    List<Purchase> findAllByCardsIdAndCardsUsersId(/*Sort sort,*/ Long cardId, Long userId);

    @Query(value = "select new com.malei.card.api.demo.dto.CardIdUserIdDto(c.users.id, p.cards.id) from Purchase p inner join p.cards c on c.id = p.cards.id where p.id = :purchaseId")
    Optional<CardIdUserIdDto> getPurchaseCardIdAndUserId(@Param("purchaseId") Long purchaseId);

}

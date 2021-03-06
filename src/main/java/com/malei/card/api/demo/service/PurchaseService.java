package com.malei.card.api.demo.service;

import com.malei.card.api.demo.dto.CardIdUserIdDto;
import com.malei.card.api.demo.dto.DebtDto;
import com.malei.card.api.demo.dto.PaymentsDto;
import com.malei.card.api.demo.model.Purchase;

import java.util.List;

public interface PurchaseService {
    Purchase getPurchaseById(String id);

    CardIdUserIdDto getPurchaseCardIdAndUserId(String purchaseId);

    Purchase updatePurchase(Purchase purchase);

    void deletePurchase(Purchase purchase);

    //List<Purchase> getAllPurchase();
    List<Purchase> getAllPurchase(List<String> params, Long userId, Boolean paid) throws Exception;

    Purchase savePurchase(Purchase purchase, String cardId);

    List<Purchase> getPurchaseByCardId(Long cardId);

    List<Purchase> getPurchaseByUserByCardId(Long cardId, Long userId);

    List<PaymentsDto> getUserPayments(String userId, String paidParam);

    DebtDto getDebtUser(String userId, String paidParam);

    List<PaymentsDto> getCardPayments(String userId, String cardId, String paidParam);

    DebtDto getDebtCard(String userId, String cardId, String paidParam);

}

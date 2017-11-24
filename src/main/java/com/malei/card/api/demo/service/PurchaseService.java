package com.malei.card.api.demo.service;

import com.malei.card.api.demo.dto.PurchaseDto;
import com.malei.card.api.demo.model.Purchase;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PurchaseService {
    Purchase getPurchaseById(Long id);
    Purchase updatePurchase(Purchase purchase);
    void deletePurchase(Purchase purchase);
    //List<Purchase> getAllPurchase();
    List<Purchase> getAllPurchase(List<String> params, Long userId, Boolean paid) throws Exception;
    Purchase savePurchase(PurchaseDto purchase) throws Exception;
    List<Purchase> getPurchaseByCardId(Long cardId);
    List<Purchase> getPurchaseByUserByCardId(Long cardId, Long userId);
}

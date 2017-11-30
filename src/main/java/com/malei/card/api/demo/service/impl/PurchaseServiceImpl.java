package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.dto.CardIdUserIdDto;
import com.malei.card.api.demo.exception.PurchaseNotFoundException;
import com.malei.card.api.demo.model.Purchase;
import com.malei.card.api.demo.repository.PurchaseRepository;
import com.malei.card.api.demo.service.CardService;
import com.malei.card.api.demo.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    CardService cardService;


    @Override
    public Purchase getPurchaseById(String id) {
        return purchaseRepository.findById(Long.parseLong(id))
                .orElseThrow(PurchaseNotFoundException::new);

    }

    @Override
    public CardIdUserIdDto getPurchaseCardIdAndUserId(String purchaseId) {
        return purchaseRepository.getPurchaseCardIdAndUserId(Long.parseLong(purchaseId))
                .orElseThrow(PurchaseNotFoundException::new);
    }

    @Override
    public Purchase savePurchase(Purchase purchase, String cardId){

        if(purchase.getDatePurchase() == null){
            purchase.setDatePurchase(LocalDate.now());
        }

        purchase.setDateOfLastPayment(purchase
                .getDatePurchase()
                .plusMonths(purchase.getMonthsInInstallments())
                .withDayOfMonth(15));

        purchase.setCards(cardService.getById(cardId));

        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getPurchaseByCardId(Long cardId) {
        return purchaseRepository
                .findAllByCards_UsersId(new Sort(Sort.Direction.DESC, "id"), cardId);
    }

    @Override
    public List<Purchase> getPurchaseByUserByCardId(Long cardId, Long userId) {
        return purchaseRepository.findAllByCardsIdAndCardsUsersId(cardId, userId);
    }

    @Override
    public Purchase updatePurchase(Purchase purchase) {
        return purchaseRepository.saveAndFlush(purchase);
    }

    @Override
    public void deletePurchase(Purchase purchase) {
        purchaseRepository.delete(purchase);
    }

//    @Override
//    public List<Purchase> getAllPurchase() {
//        return purchaseRepository.findAll();
//    }

    @Override
    public List<Purchase> getAllPurchase(List<String> params, Long userId, Boolean paid) throws Exception {

        if(!paid){
            String sort = params.get(params.size()-1);
            params.remove(params.size()-1);
            if (sort.equals("ask")){
                return purchaseRepository
                        .findAllByCards_UsersId(new Sort(Sort.Direction.ASC, params.get(0)), userId);
            } else if (sort.equals("desk")) {
                return purchaseRepository
                        .findAllByCards_UsersId(new Sort(Sort.Direction.DESC, params.get(0)), userId);
            }
        } else {
            String sort = params.get(params.size() - 1);
            params.remove(params.size() - 1);
            if (sort.equals("ask")) {
                return purchaseRepository
                        .findAllByCards_UsersIdAndDateOfLastPaymentIsBefore
                                (new Sort(Sort.Direction.ASC, params.get(0)), userId, LocalDate.now());
            } else if (sort.equals("desk")) {
                return purchaseRepository
                        .findAllByCards_UsersIdAndDateOfLastPaymentIsBefore
                                (new Sort(Sort.Direction.DESC, params.get(0)), userId, LocalDate.now());
            }
        }
        throw  new Exception();

//          return params == null ? purchaseRepository.findAllByCardsIdAndCardsUsersId(cardId, userId)
//                : params.get(params.size()-1).equals("ask")
//                ? purchaseRepository
//                  .findAllByCardsIdAndCardsUsersId(new Sort(Sort.Direction.ASC, params.get(0)), userId, cardId)
//                : purchaseRepository
//                  .findAllByCardsIdAndCardsUsersId(new Sort(Sort.Direction.DESC, params.get(0)), userId, cardId);
    }
}

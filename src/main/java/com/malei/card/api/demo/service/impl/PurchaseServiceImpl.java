package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.dto.PurchaseDto;
import com.malei.card.api.demo.model.Purchase;
import com.malei.card.api.demo.repository.PurchaseRepository;
import com.malei.card.api.demo.service.CardService;
import com.malei.card.api.demo.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static java.lang.Long.parseLong;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    CardService cardService;


    @Override
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Purchase savePurchase(PurchaseDto purchase) throws Exception {
        Purchase newPurchase = new Purchase();
        newPurchase.setName(purchase.getName());
        newPurchase.setPrice(purchase.getPrice());
        newPurchase.setMonthsInInstallments(purchase.getMonthsInInstallments());
        LocalDate today;
        if(purchase.getDatePurchase() == null){
            today = LocalDate.now();
        } else {
            today = purchase.getDatePurchase();
        }
        newPurchase.setDatePurchase(today);
        LocalDate DateOfLastPayment =
                today
                .plusMonths(purchase.getMonthsInInstallments())
                .withDayOfMonth(15);
        newPurchase.setDateOfLastPayment(DateOfLastPayment);
        newPurchase.setCards(cardService.getById(purchase.getCardId()));
        return purchaseRepository.save(newPurchase);
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

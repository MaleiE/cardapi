package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.dto.CardIdUserIdDto;
import com.malei.card.api.demo.dto.DebtDto;
import com.malei.card.api.demo.dto.PaymentsDto;
import com.malei.card.api.demo.exception.PurchaseNotFoundException;
import com.malei.card.api.demo.model.Purchase;
import com.malei.card.api.demo.repository.PurchaseRepository;
import com.malei.card.api.demo.service.CardService;
import com.malei.card.api.demo.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

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

    @Override
    public List<Purchase> getAllPurchase(List<String> params, Long userId, Boolean paid){

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
        throw  new IllegalArgumentException();
    }

    @Override
    public List<PaymentsDto> getUserPayments(String userId, Boolean paid){

        if(paid){
           List<Purchase> purchases = purchaseRepository.findAllByCards_UsersId(new Sort(Sort.Direction.DESC, "datePurchase"),Long.parseLong(userId));
           return createPaymentsList(purchases, LocalDate.now());
        }else {

            LocalDate date = purchaseRepository
                    .findAllByCards_UsersIdAndDateOfLastPaymentIsBefore(new Sort(Sort.Direction.DESC, "datePurchase"),Long.parseLong(userId), LocalDate.now())
                    .stream()
                    .map(Purchase::getDatePurchase)
                    .min(LocalDate::compareTo).orElse(LocalDate.now());

            return createPaymentsList(purchaseRepository
                    .findAllByCards_UsersIdAndDateOfLastPaymentIsBefore(new Sort(Sort.Direction.DESC, "datePurchase"), Long.parseLong(userId), LocalDate.now()), date);
        }
    }

    @Override
    public List<PaymentsDto> getCardPayments(String userId, String cardId, Boolean paid){
        List<Purchase> purchases = purchaseRepository.findAllByCardsIdAndCardsUsersId(Long.parseLong(cardId), Long.parseLong(userId));

        if(paid){
            return  createPaymentsList(purchases, LocalDate.now());
        } else {
            LocalDate date = purchaseRepository.findAllByCardsIdAndCardsUsersId(Long.parseLong(cardId), Long.parseLong(userId))
                    .stream()
                    .map(Purchase::getDatePurchase)
                    .min(LocalDate::compareTo).orElse(LocalDate.now());
            return createPaymentsList(purchases, date);
        }

    }

    @Override
    public DebtDto getDebtUser(String userId, Boolean paid) {

        DebtDto debt = new DebtDto();
        debt.setDebt(new BigDecimal(0));
        getUserPayments(userId,paid).forEach(paymentsDto ->
                debt.setDebt(debt.getDebt().add(paymentsDto.getPay()))

        );
        return debt;
    }

    private List<PaymentsDto> createPaymentsList (List<Purchase> purchases, LocalDate date) {
        purchases.forEach(System.out::println);
        System.out.println(date);

        List<PaymentsDto> paymentsDtos = new ArrayList<>();
        LocalDate maxDate = purchases.stream().map(Purchase::getDatePurchase).max(LocalDate::compareTo).orElse(date);

        for (int i = 1; i!=0;) {
            PaymentsDto paymentsDto = new PaymentsDto();
            for (Purchase p : purchases) {
                if (p.getDatePurchase().with(lastDayOfMonth()).isBefore(date.plusMonths(1)) && date.isBefore(p.getDateOfLastPayment())) {
                    paymentsDto.setDatePayment(date.withDayOfMonth(15).plusMonths(1));
                    BigDecimal bigDecimal = p.getPrice().divide(new BigDecimal(p.getMonthsInInstallments()), 2, RoundingMode.HALF_UP);
                    paymentsDto.setPay(paymentsDto.getPay().add(bigDecimal));
                }
            }
            i = paymentsDto.getPay().intValue();
            if(i!=0) {
                paymentsDtos.add(paymentsDto);
            } else if (maxDate.isAfter(date)){
                i++;
            }
            date = date.plusMonths(1);
        }
        paymentsDtos.forEach(System.out::println);

        return paymentsDtos;
    }
}

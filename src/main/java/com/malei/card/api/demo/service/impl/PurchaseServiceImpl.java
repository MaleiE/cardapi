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
    public Purchase savePurchase(Purchase purchase, String cardId) {

        if (purchase.getDatePurchase() == null) {
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
    public List<Purchase> getAllPurchase(List<String> params, Long userId, Boolean paid) {

        if (!paid) {
            String sort = params.get(params.size() - 1);
            params.remove(params.size() - 1);
            if (sort.equals("ask")) {
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
        throw new IllegalArgumentException();
    }

    @Override
    public List<PaymentsDto> getUserPayments(String userId, String paidParam) {
        List<Purchase> purchases = purchaseRepository.findAllByCards_UsersId(Long.parseLong(userId));

        return paramsPaymentList(purchases, paidParam);
    }

    @Override
    public List<PaymentsDto> getCardPayments(String userId, String cardId, String paidParam) {
        List<Purchase> purchases = purchaseRepository.findAllByCardsIdAndCardsUsersId(Long.parseLong(cardId), Long.parseLong(userId));

        return paramsPaymentList(purchases, paidParam);
    }

    @Override
    public DebtDto getDebtUser(String userId, String paidParam) {
        return getDebt(getUserPayments(userId, paidParam));
    }

    @Override
    public DebtDto getDebtCard(String userId, String cardId, String paidParam) {
        return getDebt(getCardPayments(userId, cardId, paidParam));
    }

    private DebtDto getDebt(List<PaymentsDto> paymentsDto) {
        DebtDto debt = new DebtDto();
        debt.setDebt(new BigDecimal(0));
        paymentsDto.forEach(payments ->
                debt.setDebt(debt.getDebt().add(payments.getPay()))
        );

        return debt;
    }

    private List<PaymentsDto> paramsPaymentList(List<Purchase> purchases, String paidParam) {

        LocalDate maxDate;

        switch (paidParam) {
            case "no":
                maxDate = purchases.stream()
                        .map(Purchase::getDateOfLastPayment)
                        .max(LocalDate::compareTo)
                        .orElse(LocalDate.now()); // FIXME orElse

                return createPaymentsList(purchases, LocalDate.now().withDayOfMonth(1), maxDate);
            case "yes":
                LocalDate date = purchases
                        .stream()
                        .map(Purchase::getDatePurchase)
                        .min(LocalDate::compareTo).orElse(LocalDate.now());
                maxDate = LocalDate.now();

                return createPaymentsList(purchases, date, maxDate);
            case "all":
                maxDate = purchases.stream().map(Purchase::getDateOfLastPayment).max(LocalDate::compareTo).orElse(LocalDate.now());

                LocalDate datePurchase = purchases
                        .stream()
                        .map(Purchase::getDatePurchase)
                        .min(LocalDate::compareTo).orElse(LocalDate.now());

                return createPaymentsList(purchases, datePurchase, maxDate);
            default:
                throw new IllegalArgumentException("The url parameter \"paid\" incorrect, default value: \"no\"");

        }
    }


    private List<PaymentsDto> createPaymentsList(List<Purchase> purchases, LocalDate date, LocalDate maxDate) {

        List<PaymentsDto> paymentsDtos = new ArrayList<>();

        for (int i = 1; i != 0; ) {
            PaymentsDto paymentsDto = new PaymentsDto();

            for (Purchase p : purchases) {
                if (p.getDatePurchase().with(lastDayOfMonth()).isBefore(date.plusMonths(1)) && date.isBefore(p.getDateOfLastPayment().minusMonths(1))) {
                    paymentsDto.setDatePayment(date.withDayOfMonth(15).plusMonths(1));
                    BigDecimal bigDecimal = p.getPrice().divide(new BigDecimal(p.getMonthsInInstallments()), 2, RoundingMode.HALF_UP);
                    paymentsDto.setPay(paymentsDto.getPay().add(bigDecimal));
                }
            }
            i = paymentsDto.getPay().intValue();
            if (date.isAfter(maxDate.minusMonths(1))) {
                System.out.println(date);
                System.out.println(maxDate);
                i = 0;
            }
            if (i != 0) {
                paymentsDtos.add(paymentsDto);
            } else if (maxDate.isAfter(date)) {
                i++;
            }


            System.out.println(i);
            date = date.plusMonths(1);
        }
        paymentsDtos.forEach(System.out::println);

        return paymentsDtos;
    }
}

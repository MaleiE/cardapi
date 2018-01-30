package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.dto.DebtDto;
import com.malei.card.api.demo.dto.PaymentsDto;
import com.malei.card.api.demo.repository.PurchaseRepository;
import com.malei.card.api.demo.service.PaidAndDebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PaidAndDebtServiceImpl implements PaidAndDebtService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public DebtDto getDebt(String userId, String... param) {
        // purchaseRepository.findAllByCardsIdAndCardsUsersId()
        return null;
    }

    @Override
    public DebtDto getDebt(String userId, String cardId, String... param) {
        return null;
    }

    @Override
    public List<PaymentsDto> getPayments(String userId, String... param) {
        return null;
    }

    @Override
    public List<PaymentsDto> getPayments(String userId, String cardId, String... param) {
        return null;
    }
}


package com.malei.card.api.demo.service;

import com.malei.card.api.demo.dto.DebtDto;
import com.malei.card.api.demo.dto.PaymentsDto;

import java.util.List;

public interface PaidAndDebtService {
    DebtDto getDebt(String userId, String...param);
    DebtDto getDebt(String userId, String cardId, String...param);
    List<PaymentsDto> getPayments(String userId, String...param);
    List<PaymentsDto> getPayments(String userId, String cardId, String...param);

}

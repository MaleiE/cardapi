package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.DebtDto;
import com.malei.card.api.demo.dto.PaymentsDto;
import com.malei.card.api.demo.service.PurchaseService;
import com.malei.card.api.demo.validation.IdConstraint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController

public class PaidAndDebtController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(value = "users/{id}/payments", method = RequestMethod.GET)
    public ResponseEntity<List<PaymentsDto>> getUserPayments(
            @IdConstraint(message = "invalid user ID", entity = "user")
            @PathVariable String id){
        return new ResponseEntity<List<PaymentsDto>>(purchaseService.getUserPayments(id, true), HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}/debts")
    public ResponseEntity<DebtDto> getUserDebt(
            @IdConstraint(message = "invalid user ID", entity = "user")
            @PathVariable String id){
        return new ResponseEntity<DebtDto>(purchaseService.getDebtUser(id, false), HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}/cards/{id}/debts")
    public ResponseEntity<DebtDto> getCardDebt(
            @IdConstraint(message = "invalid user ID", entity = "card")
            @PathVariable String id){
                return null;


    }

    @RequestMapping(value = "users/{userId}/cards/{cardId}/payments")
    public ResponseEntity<List<PaymentsDto>> getCardPayments(
            @IdConstraint(message = "invalid user ID", entity = "user")
            @PathVariable String userId,
            @IdConstraint(message = "invalid card ID", entity = "card")
            @PathVariable String cardId){
        return new ResponseEntity<List<PaymentsDto>>(purchaseService.  getCardPayments(userId, cardId, true), HttpStatus.OK);
    }


}

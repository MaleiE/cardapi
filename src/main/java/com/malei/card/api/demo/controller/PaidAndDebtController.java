package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.DebtDto;
import com.malei.card.api.demo.dto.PaymentsDto;
import com.malei.card.api.demo.dto.PurchaseDto;
import com.malei.card.api.demo.service.PurchaseService;
import com.malei.card.api.demo.validation.IdConstraint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Validated
@RestController
public class PaidAndDebtController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(value = "users/{id}/payments", method = RequestMethod.GET)
    public ResponseEntity<Resources<PaymentsDto>> getUserPayments(
            @IdConstraint(message = "invalid user ID", entity = "user")
            @PathVariable String id,
            @RequestParam(name = "paid", required = false, defaultValue = "no") String paid
            ){
                List<PaymentsDto> paymentsDto = purchaseService.getUserPayments(id, paid);

                Link selfLink = linkTo(methodOn(PaidAndDebtController.class).getUserPayments(id, paid)).withSelfRel();
                Link userLink = linkTo(methodOn(UserController.class).getUserById(id)).withRel("user");

                Resources<PaymentsDto> paymentsDtoResources = new Resources<>(paymentsDto);

                paymentsDtoResources.add(selfLink, userLink);
        return new ResponseEntity<Resources<PaymentsDto>>(paymentsDtoResources, HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}/debts")
    public ResponseEntity<DebtDto> getUserDebt(
            @IdConstraint(message = "invalid user ID", entity = "user")
            @PathVariable String id,
            @RequestParam(name = "paid", defaultValue = "no", required = false) String paid){
                DebtDto debtDto = purchaseService.getDebtUser(id, paid);
                Link selfLink = linkTo(methodOn(PaidAndDebtController.class).getUserDebt(id, paid)).withSelfRel();
                Link userLink = linkTo(methodOn(UserController.class).getUserById(id)).withRel("user");
                Link paymentsLink = linkTo(methodOn(PaidAndDebtController.class).getUserPayments(id,paid)).withRel("payments");
                debtDto.add(selfLink, userLink, paymentsLink);
        return new ResponseEntity<DebtDto>(debtDto, HttpStatus.OK);
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
        return new ResponseEntity<List<PaymentsDto>>(purchaseService.getCardPayments(userId, cardId, false), HttpStatus.OK);
    }


}

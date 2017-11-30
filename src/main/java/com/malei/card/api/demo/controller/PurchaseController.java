package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.CreateAndUpdatePurchaseDto;
import com.malei.card.api.demo.dto.PurchaseDto;
import com.malei.card.api.demo.dto.CardIdUserIdDto;
import com.malei.card.api.demo.model.Purchase;
import com.malei.card.api.demo.service.PurchaseService;
import com.malei.card.api.demo.validation.CardIdConstraint;
import com.malei.card.api.demo.validation.IdConstraint;
import com.malei.card.api.demo.validation.SortParamsConstraint;
import com.malei.card.api.demo.validation.UserIdConstraint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Validated
@RestController
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "users/{userId}/cards/{cardId}/purchase", method = RequestMethod.POST)
    public ResponseEntity<PurchaseDto> add(
            @UserIdConstraint
            @PathVariable(name = "userId") String userId,
            @CardIdConstraint
            @PathVariable(name = "cardId") String cardId,
            @RequestBody CreateAndUpdatePurchaseDto purchase) throws Exception {

        PurchaseDto purchaseDto = modelMapper
                .map(purchaseService
                        .savePurchase(modelMapper
                                .map(purchase,Purchase.class),cardId),PurchaseDto.class);

        Link selfLink = linkTo(methodOn(PurchaseController.class).getPurchase(purchaseDto.getPurchaseId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        Link cardLink = linkTo(methodOn(CardController.class).getCard(userId, cardId)).withRel("card");

        purchaseDto.add(selfLink, userLink, cardLink);

        return new ResponseEntity<PurchaseDto>(purchaseDto, HttpStatus.CREATED);
    }



    @RequestMapping(value = "users/{userId}/cards/{cardId}/purchase/{purId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Purchase purchase){
        return  new ResponseEntity<Purchase>(purchaseService.updatePurchase(purchase),HttpStatus.OK);
    }
    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET)
    public ResponseEntity<PurchaseDto> getPurchase(
            @IdConstraint(message = "purchase not found", entity = "purchase")
            @PathVariable String id){
        PurchaseDto purchaseDto = modelMapper.map(purchaseService.getPurchaseById(id), PurchaseDto.class);
        CardIdUserIdDto cardIdUserIdDto = purchaseService.getPurchaseCardIdAndUserId(purchaseDto.getPurchaseId().toString());

        Link selfLink = linkTo(methodOn(PurchaseController.class).getPurchase(purchaseDto.getPurchaseId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(cardIdUserIdDto.getUserId())).withRel("user");
        Link cardLink = linkTo(methodOn(CardController.class).getCard(cardIdUserIdDto.getUserId(), cardIdUserIdDto.getCardId())).withRel("card");

        purchaseDto.add(selfLink, userLink, cardLink);

        return new ResponseEntity<PurchaseDto>(purchaseDto,HttpStatus.OK);
    }

    @RequestMapping(value = "/purchase", method = RequestMethod.GET)
    public ResponseEntity<List<Purchase>> getAllPurchaseUser(
            @UserIdConstraint
            @PathVariable String  userId,
            @SortParamsConstraint
            @RequestParam(name = "sort", required = false, defaultValue = "id,ask") List<String> sortParams,
            @RequestParam(name = "paid", required = false, defaultValue = "false") String paid) throws Exception {
                    return new ResponseEntity<List<Purchase>>(purchaseService
                                    .getAllPurchase(sortParams, Long.parseLong(userId), Boolean.parseBoolean(paid)), HttpStatus.OK);
    }

    @RequestMapping(value = "/cards/{cardId}/purchase", method = RequestMethod.GET)
    public ResponseEntity<List<Purchase>> getAllPurchaseByCard(
            @UserIdConstraint
            @PathVariable String userId,
            @CardIdConstraint
            @PathVariable String cardId){
        return new ResponseEntity<List<Purchase>>(
                purchaseService.getPurchaseByUserByCardId(Long.parseLong(cardId),Long.parseLong(userId)),
                HttpStatus.OK);
    }
}
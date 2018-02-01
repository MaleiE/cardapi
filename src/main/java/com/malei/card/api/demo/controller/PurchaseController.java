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
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Validated
@RestController
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "users/{userId}/cards/{cardId}/purchase")
    public ResponseEntity<PurchaseDto> add(
            @UserIdConstraint
            @PathVariable(name = "userId") String userId,
            @CardIdConstraint
            @PathVariable(name = "cardId") String cardId,
            @RequestBody CreateAndUpdatePurchaseDto purchase) throws Exception {

        PurchaseDto purchaseDto = modelMapper
                .map(purchaseService
                        .savePurchase(modelMapper
                                .map(purchase, Purchase.class), cardId), PurchaseDto.class);

        Link selfLink = linkTo(methodOn(PurchaseController.class).getPurchase(purchaseDto.getPurchaseId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        Link cardLink = linkTo(methodOn(CardController.class).getCard(userId, cardId)).withRel("card");

        purchaseDto.add(selfLink, userLink, cardLink);

        return new ResponseEntity<PurchaseDto>(purchaseDto, HttpStatus.CREATED);
    }


    @PutMapping(value = "/purchase/{id}")
    public ResponseEntity<?> update(
            @IdConstraint(message = "purchase not found", entity = "purchase")
            @PathVariable String id,
            @RequestBody CreateAndUpdatePurchaseDto purchase) {

        PurchaseDto purchaseDto = modelMapper
                .map(purchaseService
                        .updatePurchase(modelMapper.map(purchase, Purchase.class)), PurchaseDto.class);

        CardIdUserIdDto cardIdUserIdDto = purchaseService.getPurchaseCardIdAndUserId(purchaseDto.getPurchaseId().toString());

        Link selfLink = linkTo(methodOn(PurchaseController.class).getPurchase(purchaseDto.getPurchaseId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(cardIdUserIdDto.getUserId())).withRel("user");
        Link cardLink = linkTo(methodOn(CardController.class).getCard(cardIdUserIdDto.getUserId(), cardIdUserIdDto.getCardId())).withRel("card");

        purchaseDto.add(selfLink, userLink, cardLink);

        return new ResponseEntity<PurchaseDto>(purchaseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/purchase/{id}")
    public ResponseEntity<PurchaseDto> getPurchase(
            @IdConstraint(message = "purchase not found", entity = "purchase")
            @PathVariable String id) {
        PurchaseDto purchaseDto = modelMapper.map(purchaseService.getPurchaseById(id), PurchaseDto.class);
        CardIdUserIdDto cardIdUserIdDto = purchaseService.getPurchaseCardIdAndUserId(purchaseDto.getPurchaseId().toString());

        Link selfLink = linkTo(methodOn(PurchaseController.class).getPurchase(purchaseDto.getPurchaseId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(cardIdUserIdDto.getUserId())).withRel("user");
        Link cardLink = linkTo(methodOn(CardController.class).getCard(cardIdUserIdDto.getUserId(), cardIdUserIdDto.getCardId())).withRel("card");

        purchaseDto.add(selfLink, userLink, cardLink);

        return new ResponseEntity<PurchaseDto>(purchaseDto, HttpStatus.OK);
    }

    @GetMapping(value = "users/{userId}/purchase")
    public ResponseEntity<?> getAllPurchaseUser(
            @UserIdConstraint
            @PathVariable String userId,
            @SortParamsConstraint
            @RequestParam(name = "sort", required = false, defaultValue = "id,ask") List<String> sortParams,
            @RequestParam(name = "paid", required = false, defaultValue = "false") String paid) throws Exception {
        Type listType = new TypeToken<List<PurchaseDto>>() {
        }.getType();
        List<PurchaseDto> purchaseDtoList = modelMapper
                .map(purchaseService.getAllPurchase(sortParams, Long.parseLong(userId), Boolean.parseBoolean(paid)), listType);

        List<PurchaseDto> purchaseDtos = addLinkPurchase(purchaseDtoList, userId);

        Resources<PurchaseDto> purchaseDtoResources = new Resources<>(purchaseDtos);

        Link selfLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseUser(userId, sortParams, paid)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");

        purchaseDtoResources.add(selfLink, userLink);

        return new ResponseEntity<>(purchaseDtoResources, HttpStatus.OK);
    }

    @GetMapping(value = "users/{userId}/cards/{cardId}/purchase")
    public ResponseEntity<?> getAllPurchaseByCard(
            @UserIdConstraint
            @PathVariable String userId,
            @CardIdConstraint
            @PathVariable String cardId) {
        Type listType = new TypeToken<List<PurchaseDto>>() {
        }.getType();
        List<PurchaseDto> purchaseDtoList = modelMapper
                .map(purchaseService.getPurchaseByUserByCardId(Long.parseLong(cardId), Long.parseLong(userId)), listType);

        List<PurchaseDto> purchaseDtos = addLinkPurchase(purchaseDtoList, userId);

        Resources<PurchaseDto> purchaseDtoResources = new Resources<>(purchaseDtos);

        Link selfLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseByCard(userId, cardId)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");

        purchaseDtoResources.add(selfLink, userLink);

        return new ResponseEntity<>(purchaseDtoResources, HttpStatus.OK);
    }

    private List<PurchaseDto> addLinkPurchase(List<PurchaseDto> purchases, String userId) {
        return purchases.stream().peek((p) -> {
            CardIdUserIdDto cardIdUserIdDto = purchaseService.getPurchaseCardIdAndUserId(p.getPurchaseId().toString());

            Link selfLink = linkTo(methodOn(PurchaseController.class).getPurchase(p.getPurchaseId().toString())).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
            Link cardLink = linkTo(methodOn(CardController.class).getCard(cardIdUserIdDto.getUserId(), cardIdUserIdDto.getCardId())).withRel("card");

            p.add(selfLink, userLink, cardLink);
        }).collect(Collectors.toList());
    }

}
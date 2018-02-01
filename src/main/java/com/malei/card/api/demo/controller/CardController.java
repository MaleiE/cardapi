package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.CardDto;
import com.malei.card.api.demo.dto.CreateAndUpdateCardDto;
import com.malei.card.api.demo.model.Card;
import com.malei.card.api.demo.service.CardService;
import com.malei.card.api.demo.validation.CardIdConstraint;
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
@RequestMapping("users/{userId}/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    ResponseEntity<CardDto> addCard(
            @UserIdConstraint
            @PathVariable String userId,
            @RequestBody CreateAndUpdateCardDto card) {
        CardDto cardDto = modelMapper.map(cardService.saveCard(modelMapper.map(card, Card.class), userId), CardDto.class);

        Link selfLink = linkTo(methodOn(CardController.class).getCard(userId, cardDto.getCardId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        Link purchaseLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseByCard(cardDto.getCardId().toString(), userId)).withRel("purchase");

        cardDto.add(selfLink, userLink, purchaseLink);
        return new ResponseEntity<CardDto>(cardDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{cardId}")
    ResponseEntity<CardDto> getCard(
            @UserIdConstraint
            @PathVariable String userId,
            @CardIdConstraint
            @PathVariable String cardId) {
        CardDto cardDto = modelMapper.map(cardService.getById(cardId), CardDto.class);
        Link selfLink = linkTo(methodOn(CardController.class).getCard(userId, cardId)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        Link purchaseLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseByCard(userId, cardId)).withRel("purchase");
        cardDto.add(selfLink, userLink, purchaseLink);
        return new ResponseEntity<CardDto>(cardDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{cardId}")
    ResponseEntity<CardDto> updateCard(
            @UserIdConstraint
            @PathVariable String userId,
            @CardIdConstraint
            @PathVariable String cardId,
            @RequestBody CreateAndUpdateCardDto card) {
        Card updateCard = modelMapper.map(card, Card.class);
        updateCard.setId(Long.parseLong(cardId));
        CardDto cardDto = modelMapper.map(cardService.updateCard(updateCard), CardDto.class);

        Link selfLink = linkTo(methodOn(CardController.class).getCard(userId, cardDto.getCardId().toString())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        Link purchaseLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseByCard(cardDto.getCardId().toString(), userId)).withRel("purchase");

        cardDto.add(selfLink, userLink, purchaseLink);
        return new ResponseEntity<CardDto>(cardDto, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{cardId}")
    ResponseEntity<Void> delete(
            @CardIdConstraint
            @PathVariable String cardId,
            @UserIdConstraint
            @PathVariable String userId) throws Exception {
        cardService.delete(cardService.getById(cardId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    ResponseEntity<?> getAllCard(
            @UserIdConstraint
            @PathVariable String userId) {
        Type listType = new TypeToken<List<CardDto>>() {
        }.getType();
        List<CardDto> cardDtos = modelMapper.map(cardService.getAllCardUser(userId), listType);

        List<CardDto> cardDtos1 = cardDtos.stream().peek((card) -> {
            Link selfLink = linkTo(methodOn(CardController.class).getCard(userId, card.getCardId().toString())).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
            Link purchaseLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseByCard(card.getCardId().toString(), userId)).withRel("purchase");
            card.add(selfLink, userLink, purchaseLink);
        }).collect(Collectors.toList());
        Resources<CardDto> cardDtoResources = new Resources<CardDto>(cardDtos1);

        Link selfLink = linkTo(methodOn(CardController.class).getAllCard(userId)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        cardDtoResources.add(selfLink, userLink);

        return new ResponseEntity<>(cardDtoResources, HttpStatus.OK);
    }
}

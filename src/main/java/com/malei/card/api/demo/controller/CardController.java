package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.CardDto;
import com.malei.card.api.demo.model.Card;
import com.malei.card.api.demo.service.impl.ImplCardService;
import com.malei.card.api.demo.validation.CardIdConstraint;
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
@RequestMapping("users/{userId}/cards")
public class CardController {

    @Autowired
    private ImplCardService cardService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(
            @UserIdConstraint
            @PathVariable String userId,
            @RequestBody Card card){
        Card newCard = new Card();
        newCard.setName(card.getName());
        newCard.setLimitCard(card.getLimitCard());
        return new  ResponseEntity<>(cardService.saveCard(newCard), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{cardId}", method = RequestMethod.GET)
    ResponseEntity<?> getCard(
            @UserIdConstraint
            @PathVariable String userId,
            @CardIdConstraint
            @PathVariable String cardId){
        CardDto cardDto = modelMapper.map(cardService.getById(cardId),CardDto.class);
        Link selfLink = linkTo(methodOn(CardController.class).getCard(userId,cardId)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        // TODO: 23.11.2017 add get purchasebycard 
        Link purchaseLink = linkTo(methodOn(PurchaseController.class).getAllPurchaseByCard(cardId, userId)).withRel("purchase");
        cardDto.add(selfLink,userLink,purchaseLink);
        return new ResponseEntity<CardDto>(cardDto,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> update(@RequestBody Card card){
        return new ResponseEntity<Card>(cardService.updateCard(card),HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
        ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        cardService.delete(cardService.getById(id.toString()));
            return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Card>> getAll(
            @UserIdConstraint
            @PathVariable String userId) {
        return  new ResponseEntity<List<Card>>(cardService.getAllCardUser(userId),HttpStatus.OK);
    }



}

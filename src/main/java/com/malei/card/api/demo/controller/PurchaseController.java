package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.PurchaseDto;
import com.malei.card.api.demo.model.Purchase;
import com.malei.card.api.demo.service.PurchaseService;
import com.malei.card.api.demo.validation.CardIdConstraint;
import com.malei.card.api.demo.validation.SortParamsConstraint;
import com.malei.card.api.demo.validation.UserIdConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;


@Validated
@RestController
@RequestMapping(value = "users/{userId}", consumes="application/json")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping(value = "/cardId/purchase", method = RequestMethod.POST)
    public ResponseEntity<?> add(
            @UserIdConstraint
            @PathVariable(name = "userId") String userId,
            @RequestBody Purchase purchase) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setUserId(userId);
        purchaseDto.setMonthsInInstallments(purchase.getMonthsInInstallments());
        purchaseDto.setPrice(purchase.getPrice());
        purchaseDto.setName(purchase.getName());

        return new ResponseEntity<Purchase>(purchaseService.savePurchase(purchaseDto), headers,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Purchase purchase){
        return  new ResponseEntity<Purchase>(purchaseService.updatePurchase(purchase),HttpStatus.OK);
    }
    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET)
    public ResponseEntity<Purchase> getPurchase(@PathVariable Long id){
        return new ResponseEntity<Purchase>(purchaseService.getPurchaseById(id),HttpStatus.OK);
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

    @RequestMapping(value = "/cards/{cardId}/purchase")
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
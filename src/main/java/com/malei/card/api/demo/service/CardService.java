package com.malei.card.api.demo.service;

import com.malei.card.api.demo.model.Card;

import java.util.List;

public interface CardService {
    Card saveCard(Card card);
    Card getById(String id);
    Card updateCard(Card card);
    void delete(Card card);
    List<Card> getAllCardUser(String userId);
}

package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.exception.CardNotFoundException;
import com.malei.card.api.demo.exception.UserNotFoundException;
import com.malei.card.api.demo.model.Card;
import com.malei.card.api.demo.repository.CardRepository;
import com.malei.card.api.demo.service.CardService;
import com.malei.card.api.demo.service.PurchaseService;
import com.malei.card.api.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

//import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.security.acl.LastOwnerException;
import java.util.List;

@Service
@Transactional
public class ImplCardService implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserService userService;

    @Override
    public Card saveCard(Card card, String userId) {
        card.setUsers(userService.getById(userId));
        return cardRepository.save(card);
    }

    @Override
    public Card getById(String id) {
        return cardRepository.findById(Long.parseLong(id))
                .orElseThrow(CardNotFoundException::new);
    }

    @Override
    public Card updateCard(Card card) {
        return cardRepository.saveAndFlush(card);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }

    @Override
    public List<Card> getAllCardUser(String userId) {
        return cardRepository.findAllByUsersId(new Sort(Sort.Direction.DESC, "id"), Long.parseLong(userId));
    }
}

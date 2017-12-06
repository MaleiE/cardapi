package com.malei.card.api.demo.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CardIdUserIdDto {
    private String userId;
    private String cardId;

    public CardIdUserIdDto(Long userId, Long cardId) {
        this.userId = userId.toString();
        this.cardId = cardId.toString();
    }

    public CardIdUserIdDto() {
    }

}

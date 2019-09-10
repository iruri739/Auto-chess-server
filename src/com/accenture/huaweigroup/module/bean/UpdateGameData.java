package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;

import java.util.List;

public class UpdateGameData {
    private String gameId;
    private int playerId;
    private List<Chess> cards;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Chess> getCards() {
        return cards;
    }

    public void setCards(List<Chess> cards) {
        this.cards = cards;
    }
}

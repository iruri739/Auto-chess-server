package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;

import java.util.List;

public class UpdateGameData {
    private String gameId;
    private int playerId;
    private List<Chess> Cards;

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
        return Cards;
    }

    public void setCards(List<Chess> cards) {
        Cards = cards;
    }
}

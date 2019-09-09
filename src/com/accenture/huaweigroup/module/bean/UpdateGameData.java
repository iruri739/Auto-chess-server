package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;

import java.util.List;

public class UpdateGameData {
    private String gameId;
    private int playerId;
    private List<Integer> battleCards;
    private List<Integer> handCards;
    private List<Integer> cardInventory;

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

    public List<Integer> getBattleCards() {
        return battleCards;
    }

    public void setBattleCards(List<Integer> battleCards) {
        this.battleCards = battleCards;
    }

    public List<Integer> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Integer> handCards) {
        this.handCards = handCards;
    }

    public List<Integer> getCardInventory() {
        return cardInventory;
    }

    public void setCardInventory(List<Integer> cardInventory) {
        this.cardInventory = cardInventory;
    }
}

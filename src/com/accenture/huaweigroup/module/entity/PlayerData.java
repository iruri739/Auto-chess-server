package com.accenture.huaweigroup.module.entity;

import java.util.List;

public class PlayerData {
    private int id;//玩家id
    private int hp;//玩家生命值
    private int gold;//玩家金币
    private int winCount;//玩家连胜数
    private List<Chess> handCards;//玩家手牌
    private List<Chess> battleCards;//玩家战场布局
    private List<Chess> cardInventory;//玩家待选区卡牌

    public PlayerData() {
        super();
    }

    public PlayerData(int id, int hp, int gold, int winCount, List<Chess> handCards, List<Chess> battleCards, List<Chess> cardInventory) {
        super();
        this.id = id;
        this.hp = hp;
        this.gold = gold;
        this.winCount = winCount;
        this.handCards = handCards;
        this.battleCards = battleCards;
        this.cardInventory = cardInventory;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "id=" + id +
                ", hp=" + hp +
                ", gold=" + gold +
                ", winCount=" + winCount +
                ", handCards=" + handCards +
                ", battleCards=" + battleCards +
                ", cardInventory=" + cardInventory +
                '}';
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public List<Chess> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Chess> handCards) {
        this.handCards = handCards;
    }

    public List<Chess> getBattleCards() {
        return battleCards;
    }

    public void setBattleCards(List<Chess> battleCards) {
        this.battleCards = battleCards;
    }

    public List<Chess> getCardInventory() {
        return cardInventory;
    }

    public void setCardInventory(List<Chess> cardInventory) {
        this.cardInventory = cardInventory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

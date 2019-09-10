package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Player {
    private static final int PLAYER_TOTAL_HP = 20;
    private static final int PLAYER_INIT_GOLD = 20;

    private int id;
    private String name;
    private int hp = PLAYER_TOTAL_HP;
    private int gold = PLAYER_INIT_GOLD;
    private int winCount = 0;

    private ArrayList<Chess> handCards = new ArrayList<>();
    private ArrayList<Chess> battleCards = new ArrayList<>();
    private ArrayList<Chess> cardInventory = new ArrayList<>();

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", gold=" + gold +
                ", winCount=" + winCount +
                ", handCards=" + handCards +
                ", battleCards=" + battleCards +
                ", cardInventory=" + cardInventory +
                '}';
    }

    public Player() {
        super();
        this.id = 0;
    }

    public Player(int id) {
        super();
        this.id = id;
    }

    public Player(Player player) {
        this.id = player.id;
        this.name = player.name;
        this.hp = player.hp;
        this.gold = player.gold;
        this.winCount = player.winCount;
        ArrayList<Chess> cacheList = player.handCards;
        for (Chess c : cacheList) {
            this.handCards.add(new Chess(c));
        }
        cacheList = player.battleCards;
        for (Chess c : cacheList) {
            this.battleCards.add(new Chess(c));
        }
        cacheList = player.cardInventory;
        for (Chess c : cacheList) {
            this.cardInventory.add(new Chess(c));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Chess> getHandCards() {
        return handCards;
    }

    public void setHandCards(ArrayList<Chess> handCards) {
        this.handCards = handCards;
    }

    public ArrayList<Chess> getBattleCards() {
        return battleCards;
    }

    public void setBattleCards(ArrayList<Chess> battleCards) {
        this.battleCards = battleCards;
    }

    public ArrayList<Chess> getCardInventory() {
        return cardInventory;
    }

    public void setCardInventory(ArrayList<Chess> cardInventory) {
        this.cardInventory = cardInventory;
    }

}

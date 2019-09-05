package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Player {
    private static final int PLAYER_TOTAL_HP = 100;
    private static final int PLAYER_INIT_GOLD = 5;

    private int id;
    private String name;
    private int hp = PLAYER_TOTAL_HP;
    private int gold = PLAYER_INIT_GOLD;
    private int winCount = 0;

    private PlayerState state = PlayerState.NONE;

    private ArrayList<Chess> handCards = new ArrayList<>();
    private ArrayList<Chess> battleCards = new ArrayList<>();
    private ArrayList<Chess> cardInventory = new ArrayList<>();

    public Player() {
        super();
        this.id = 0;
    }

    public Player(int id) {
        super();
        this.id = id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", gold=" + gold +
                ", winCount=" + winCount +
                ", state=" + state +
                ", handCards=" + handCards +
                ", battleCards=" + battleCards +
                ", cardInventory=" + cardInventory +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
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

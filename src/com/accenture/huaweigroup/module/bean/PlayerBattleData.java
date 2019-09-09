package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PlayerBattleData {
    private int playerOneId;
    private int playerTwoId;
    private ArrayList<Chess> playerOneBattleCards;
    private ArrayList<Chess> playerTwoBattleCards;

    public int getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(int playerOneId) {
        this.playerOneId = playerOneId;
    }

    public int getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(int playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public ArrayList<Chess> getPlayerOneBattleCards() {
        return playerOneBattleCards;
    }

    public void setPlayerOneBattleCards(ArrayList<Chess> playerOneBattleCards) {
        this.playerOneBattleCards = playerOneBattleCards;
    }

    public ArrayList<Chess> getPlayerTwoBattleCards() {
        return playerTwoBattleCards;
    }

    public void setPlayerTwoBattleCards(ArrayList<Chess> playerTwoBattleCards) {
        this.playerTwoBattleCards = playerTwoBattleCards;
    }
}

package com.accenture.huaweigroup.module.bean;

import org.springframework.stereotype.Component;


@Component
public class BattleData {
    private int state;
    private String gameId;
    private int prepareTime;
    private int battleTime;
    private int rounds;
    private Player playerOneData;
    private Player playerTwoData;

    public BattleData() {
        super();
    }

    public BattleData(Game game) {
        state = 0;
        gameId = game.getId();
        rounds = game.getRounds();
        prepareTime = game.getPrepareTime();
        battleTime = game.getBattleTime();
        playerOneData = game.getPlayerOne();
        playerTwoData = game.getPlayerTwo();
    }

    @Override
    public String toString() {
        return "BattleData{" +
                "state=" + state +
                ", gameId=" + gameId +
                ", prepareTime=" + prepareTime +
                ", battleTime=" + battleTime +
                ", rounds=" + rounds +
                ", playerOneData=" + playerOneData +
                ", playerTwoData=" + playerTwoData +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public int getBattleTime() {
        return battleTime;
    }

    public void setBattleTime(int battleTime) {
        this.battleTime = battleTime;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public Player getPlayerOneData() {
        return playerOneData;
    }

    public void setPlayerOneData(Player playerOneData) {
        this.playerOneData = playerOneData;
    }

    public Player getPlayerTwoData() {
        return playerTwoData;
    }

    public void setPlayerTwoData(Player playerTwoData) {
        this.playerTwoData = playerTwoData;
    }
}

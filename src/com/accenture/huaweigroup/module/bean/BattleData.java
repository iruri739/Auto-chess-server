package com.accenture.huaweigroup.module.bean;

import org.springframework.stereotype.Component;


@Component
public class BattleData {
    private GameState state;
    private String gameId;
    private int prepareTime;
    private int rounds;
    private Player playerOneData;
    private Player playerTwoData;

    public BattleData() {
        super();
    }

    public BattleData(Game game) {
        state = game.getState();
        gameId = game.getId();
        rounds = game.getRounds();
        prepareTime = game.getPrepareTime();
        playerOneData = game.getPlayerOne();
        playerTwoData = game.getPlayerTwo();
    }

    @Override
    public String toString() {
        return String.format("游戏数据对象：\n{\n状态：%s\n游戏ID： %s\n" +
                "游戏轮数： %d\n剩余准备时间： %d\n}\n", state, gameId, rounds, prepareTime);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
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

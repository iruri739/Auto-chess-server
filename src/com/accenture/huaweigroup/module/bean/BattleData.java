package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.business.ChessManager;
import org.springframework.stereotype.Component;


@Component
public class BattleData {
    private GameState state;
    private String gameId;
    private int prepareTime;
    private int rounds;
    private boolean isCached = false;
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
        playerOneData = new Player(game.getPlayerOne());
        playerTwoData = new Player(game.getPlayerTwo());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("游戏数据对象：\n");
        builder.append(String.format("状态： %s 游戏ID： %s 游戏轮数： %d 剩余准备时间： %d\n",
                state, gameId, rounds, prepareTime));
        builder.append("playerOne battleCards: \n");
        builder.append(ChessManager.formatShowChessList(playerOneData.getBattleCards()));
        builder.append("playerTwo battleCards: \n");
        builder.append(ChessManager.formatShowChessList(playerTwoData.getBattleCards()));
        return builder.toString();
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
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

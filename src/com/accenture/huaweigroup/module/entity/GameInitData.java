package com.accenture.huaweigroup.module.entity;

public class GameInitData {
    private int gameId;//当局游戏id
    private int opponentPlayerId;//对手玩家id
    private String opponentPlayerName;//对手玩家用户名
    private PlayerData playerData;//玩家自身信息

    @Override
    public String toString() {
        return "GameInitData{" +
                "gameId=" + gameId +
                ", opponentPlayerId=" + opponentPlayerId +
                ", opponentPlayerName='" + opponentPlayerName + '\'' +
                ", playerData=" + playerData +
                '}';
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getOpponentPlayerId() {
        return opponentPlayerId;
    }

    public void setOpponentPlayerId(int opponentPlayerId) {
        this.opponentPlayerId = opponentPlayerId;
    }

    public String getOpponentPlayerName() {
        return opponentPlayerName;
    }

    public void setOpponentPlayerName(String opponentPlayerName) {
        this.opponentPlayerName = opponentPlayerName;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }
}

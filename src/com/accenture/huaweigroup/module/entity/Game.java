package com.accenture.huaweigroup.module.entity;

import com.accenture.huaweigroup.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Game {
    private static final int PLAYER_DEFAULT_PREPARETIME = 30;
    private static final int BATTLE_DEFAULT_TIME = 60;

    @Autowired
    private ChessService chessService;

    private int id;
    private int totalTime = 0;
    private int rounds = 1;
    private int prepareTime = PLAYER_DEFAULT_PREPARETIME;
    private boolean startPrepare = false;
    private int battleTime = BATTLE_DEFAULT_TIME;
    private boolean startBattle = false;
    private GameState state = GameState.CREATED;
    private ConcurrentHashMap<Integer, Player> playerList = new ConcurrentHashMap<>();

    public Game() {
        super();
    }

    public Game(int id, int playerOneId, int playerTwoId) {
        super();
        this.id = id;
        this.playerList.put(playerOneId, new Player(playerOneId));
        this.playerList.put(playerTwoId, new Player(playerTwoId));
    }

    @Scheduled(fixedDelay = 1000)
    private void checkTimeState() {
        if (checkPlayerState(PlayerState.PREPARE)) {
            startPrepare = true;
        }
        if (checkPlayerState(PlayerState.BATTLE)) {
            startBattle = true;
        }
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 1000)
    private void circlePrepareTime() {
        if (startPrepare) {
            prepareTime--;
            System.out.println("剩余准备时间：" + prepareTime);
            if (prepareTime == 0) {
                startPrepare = false;
                prepareTime = PLAYER_DEFAULT_PREPARETIME;
            }
        }
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 1000)
    private void circleBattleTime() {
        if (startBattle) {
            battleTime--;
            System.out.println("剩余战斗时间：" + battleTime);
            if (battleTime == 0) {
                startBattle = false;
                battleTime = BATTLE_DEFAULT_TIME;
            }
        }
    }


    public boolean containPlayer(int playerId) {
        for (Player player : playerList.values()) {
            if (player.getId() == playerId) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPlayerState(PlayerState state) {
        for (Player player : playerList.values()) {
            if (player.getState() != state) {
                return false;
            }
        }
        return true;
    }

    public Player getPlayer(int playerId) {
        return playerList.get(playerId);
    }

    public Player getConponentPlayer(int playerId) {
        for (Player player : playerList.values()) {
            if (player.getId() != playerId) {
                return player;
            }
        }
        return null;
    }

    //双方玩家战场上卡牌的战斗处理
    public int fight() {
        return 0;
    }

    //花费金币更换玩家待选区卡牌，会返回是否成功
    public boolean randInventory(int userId) {
        return false;
    }

    //获取玩家手牌
    public ArrayList<Chess> getPlayerHandCards(int playerId) {
        return playerList.get(playerId).getHandCards();
    }

    //获取玩家战场卡牌
    public ArrayList<Chess> getPlayerBattleCards(int playerId) {
        return playerList.get(playerId).getBattleCards();
    }

    //获取玩家待选区卡牌
    public ArrayList<Chess> getPlayerCardInventory(int playerId) {
        return playerList.get(playerId).getCardInventory();
    }

    //更新玩家手牌
    public ArrayList<Chess> setPlayerHandCards(int playerId, ArrayList<Chess> newHandCards) {
        Player player = playerList.get(playerId);
        player.setHandCards(newHandCards);
        return player.getHandCards();
    }

    //更新玩家战场卡牌
    public ArrayList<Chess> setPlayerBattleCards(int playerId, ArrayList<Chess> newBattleCards) {
        Player player = playerList.get(playerId);
        player.setBattleCards(newBattleCards);
        return player.getBattleCards();
    }

    //更新玩家待选区卡牌
    public ArrayList<Chess> setPlayerCardInventory(int playerId, ArrayList<Chess> newInventory) {
        Player player = playerList.get(playerId);
        player.setCardInventory(newInventory);
        return player.getCardInventory();
    }

    public ConcurrentHashMap<Integer, Player> getPlayerList() {
        return playerList;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

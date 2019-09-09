package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.bean.GameState;
import com.accenture.huaweigroup.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class Game {
    private static final int PLAYER_DEFAULT_PREPARETIME = 30;
    private static final int BATTLE_DEFAULT_TIME = 60;
    private static final ScheduledExecutorService scheduleService = Executors.newSingleThreadScheduledExecutor();

    private String id;
    private int totalTime = 0;
    private int rounds = 1;
    private int prepareTime = PLAYER_DEFAULT_PREPARETIME;
    private boolean startPrepare = true;
    private int battleTime = BATTLE_DEFAULT_TIME;
    private boolean startBattle = true;
    private GameState state = GameState.CREATED;
    private Player playerOne = new Player();
    private Player playerTwo = new Player();

    public Game() {
        super();
    }

    public Game(String id, int playerOneId, int playerTwoId) {
        super();
        this.id = id;
        this.playerOne.setId(playerOneId);
        this.playerTwo.setId(playerTwoId);
    }

//    @Scheduled(initialDelay = 0, fixedRate = 1000)
//    private void readyToPrepare() {
//        if (state != GameState.PREPARE) {
//            state = GameState.GAMING;
//        } else if (state == GameState.PREPARE) {
//
//        }
//    }

    @Scheduled(initialDelay = 0, fixedRate = 1000)
    private void stateChange() {
//        判断双方玩家是否进入准备阶段，进入则回合数增加，后期加入其它处理
        if (checkPlayerState(PlayerState.PREPARE)) {
            rounds++;
            state = GameState.GAMING;
        }
        //判断双方玩家是否进入战斗阶段，进入则服务器进行逻辑战斗，处理相关数据
        if (checkPlayerState(PlayerState.BATTLE)) {
            fight();
            state = GameState.GAMING;
        }
    }


    public boolean containPlayer(int playerId) {
        if (playerOne.getId() == playerId || playerTwo.getId() == playerId) {
            return true;
        }
        return false;
    }

    public boolean checkPlayerState(PlayerState state) {
        if (playerOne.getState() == state && playerTwo.getState() == state) {
            return true;
        }
        return false;
    }

    public void resetTime() {
        this.prepareTime = PLAYER_DEFAULT_PREPARETIME;
        this.playerOne.setState(PlayerState.PREPARE);
        this.playerTwo.setState(PlayerState.PREPARE);
    }

    public Player getPlayer(int playerId) {
        if (playerOne.getId() == playerId) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

    public Player getConponentPlayer(int playerId) {
        if (playerOne.getId() != playerId) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

    private void cardFight(Chess A, Chess B) {
        while (A.isAlive() && B.isAlive()) {
            int Acost = B.getAttack() - A.getHp();
            int Bcost = A.getAttack() - B.getHp();
            if (Acost < 0) {
                A.setHp(Acost);
            } else {
                A.setHp(0);
                A.setAlive(false);
            }
            if (Bcost < 0) {
                B.setHp(Bcost);
            } else {
                B.setHp(0);
                B.setAlive(false);
            }
        }
    }

    private int findOtherChess(ArrayList<Chess> cards, int pos) {
        int finder = pos;
        while (finder >= 0) {
            finder--;
            if (cards.get(finder).isAlive()) {
                return finder;
            }
        }
        finder = pos;
        while (finder < cards.size()) {
            finder++;
            if (cards.get(finder).isAlive()) {
                return finder;
            }
        }
        return -1;
    }

    private int simpleFindChess(ArrayList<Chess> chess) {
        for (Chess i : chess) {
            if (i.isAlive()) {
                return chess.indexOf(i);
            }
        }
        return -1;
    }

    private void findChessAndFight(ArrayList<Chess> Acards, ArrayList<Chess> Bcards, int pos) {
        Chess A = Acards.get(pos);
        Chess B = Bcards.get(pos);
        if (A.isAlive() && B.isAlive()) {
            cardFight(A, B);
        } else if (A.isAlive() && !B.isAlive()) {
            int finder = findOtherChess(Bcards, pos);
            if (finder != -1) {
                cardFight(A, Bcards.get(finder));
            }
        } else if (!A.isAlive() && B.isAlive()) {
            int finder = findOtherChess(Acards, pos);
            if (finder != -1) {
                cardFight(B, Acards.get(finder));
            }
        }
    }

    private boolean noCardsToBattle(int who) {
        boolean flag = true;
        if (who == 1) {
            for (Chess c : playerOne.getBattleCards()) {
                if (c.isAlive()) {
                    flag = false;
                    break;
                }
            }
        } else if (who == 2) {
            for (Chess c : playerTwo.getBattleCards()) {
                if (c.isAlive()) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    private void battleResult() {
        if (noCardsToBattle(1)) {
            int count = 0;
            for (Chess c : playerTwo.getBattleCards()) {
                if (c.isAlive()) {
                    count++;
                }
            }
            playerOne.setHp(playerOne.getHp() - count * 2);
            if (playerOne.getWinCount() != 0) {
                playerOne.setWinCount(0);
            }
            playerTwo.setWinCount(playerTwo.getWinCount() + 1);
        } else {
            int count = 0;
            for (Chess c : playerOne.getBattleCards()) {
                if (c.isAlive()) {
                    count++;
                }
            }
            playerTwo.setHp(playerTwo.getHp() - count * 2);
            if (playerTwo.getWinCount() != 0) {
                playerTwo.setWinCount(0);
            }
            playerOne.setWinCount(playerOne.getWinCount() + 1);
        }
    }

    private void battleFinishCheck() {
        if (playerOne.getHp() <= 0 || playerTwo.getHp() <= 0) {
            state = GameState.FINISHED;
        }
    }

    private void refeashGold() {
        if (playerOne.getGold() < 50) {
            int extraGold = playerOne.getGold() / 10 * 2;
            playerOne.setGold(playerOne.getGold() + extraGold);
        } else {
            playerOne.setGold(playerOne.getGold() + 10);
        }
    }

    //双方玩家战场上卡牌的战斗处理
    private void fight() {
        ArrayList<Chess> chessOne = playerOne.getBattleCards();
        ArrayList<Chess> chessTwo = playerTwo.getBattleCards();
        Random random = new Random();
        while (noCardsToBattle(1) && noCardsToBattle(2)) {
            if (chessOne.size() >= chessTwo.size()) {
                for (int i = 0; i < chessOne.size(); ++i) {
                    if (i < chessTwo.size()) {
                        findChessAndFight(chessOne, chessTwo, i);
                    } else {
                        int finder = simpleFindChess(chessTwo);
                        if (finder != -1) {
                            cardFight(chessOne.get(i), chessTwo.get(finder));
                        }
                    }
                }
            } else {
                for (int i = 0; i < chessTwo.size(); ++i) {
                    if (i < chessOne.size()) {
                        findChessAndFight(chessOne, chessTwo, i);
                    } else {
                        int finder = simpleFindChess(chessOne);
                        if (finder != -1) {
                            cardFight(chessOne.get(i), chessOne.get(finder));
                        }
                    }
                }
            }
        }
        battleResult();
        refeashGold();
    }

    //获取玩家手牌
    public ArrayList<Chess> getPlayerHandCards(int playerId) {
        Player player = getPlayer(playerId);
        return player.getHandCards();
    }

    //获取玩家战场卡牌
    public ArrayList<Chess> getPlayerBattleCards(int playerId) {
        Player player = getPlayer(playerId);
        return player.getBattleCards();
    }

    //获取玩家待选区卡牌
    public ArrayList<Chess> getPlayerCardInventory(int playerId) {
        Player player = getPlayer(playerId);
        return player.getCardInventory();
    }

    //更新玩家手牌
    public ArrayList<Chess> setPlayerHandCards(int playerId, ArrayList<Chess> newHandCards) {
        Player player = getPlayer(playerId);
        player.setHandCards(newHandCards);
        return player.getHandCards();
    }

    //更新玩家战场卡牌
    public ArrayList<Chess> setPlayerBattleCards(int playerId, ArrayList<Chess> newBattleCards) {
        Player player = getPlayer(playerId);
        player.setBattleCards(newBattleCards);
        return player.getBattleCards();
    }

    //更新玩家待选区卡牌
    public ArrayList<Chess> setPlayerCardInventory(int playerId, ArrayList<Chess> newInventory) {
        Player player = getPlayer(playerId);
        player.setCardInventory(newInventory);
        return player.getCardInventory();
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public boolean isStartPrepare() {
        return startPrepare;
    }

    public void setStartPrepare(boolean startPrepare) {
        this.startPrepare = startPrepare;
    }

    public boolean isStartBattle() {
        return startBattle;
    }

    public void setStartBattle(boolean startBattle) {
        this.startBattle = startBattle;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}

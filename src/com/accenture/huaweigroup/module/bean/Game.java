package com.accenture.huaweigroup.module.bean;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.bean.GameState;
import com.accenture.huaweigroup.service.ChessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class Game{
    public static final int PLAYER_DEFAULT_PREPARETIME = 30;

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    //计算战斗结果的系统时间
    private Date calEndDT = null;
    //当前回合倒计时的时长
    private int lastTime = PLAYER_DEFAULT_PREPARETIME;
    private String id;
    private int totalTime = 0;
    private int rounds = 1;
    private int prepareTime = PLAYER_DEFAULT_PREPARETIME;
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

    public Player getPlayer(int playerId) {
        if (playerOne.getId() == playerId) {
            return playerOne;
        } else if (playerTwo.getId() == playerId) {
            return playerTwo;
        }
        return null;
    }

    public Player getOtherPlayer(int playerId) {
        if (playerOne.getId() == playerId) {
            return playerTwo;
        } else if (playerTwo.getId() == playerId) {
            return playerOne;
        }
        return null;
    }

    public void calcLastTime() {
        if (this.calEndDT != null) {
            this.lastTime = (int) (PLAYER_DEFAULT_PREPARETIME - (new Date().getTime() - this.calEndDT.getTime()));
            this.prepareTime = this.lastTime;
        }
    }

    public void cacheBattle() {
        playerOne.setCacheBattleCards(playerOne.getBattleCards());
        playerTwo.setCacheBattleCards(playerTwo.getBattleCards());
    }

    /**
     * 卡牌战斗逻辑
     * 战斗会一直持续到双方卡牌某一方死亡
     *
     * @param A
     * @param B
     */
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

    /**
     * 寻找所在位置左边和右边的存活卡牌
     * 优先从左边开始寻找
     *
     * @param cards
     * @param pos
     * @return
     */
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

    /**
     * 简单寻找场上存活的卡牌
     * 用于处理自身卡牌没有对位卡牌可供战斗时的情况
     * 从对方存活卡牌中寻找一位仍然存活的卡牌并战斗
     *
     * @param chess
     * @return
     */
    private int simpleFindChess(ArrayList<Chess> chess) {
        for (Chess i : chess) {
            if (i.isAlive()) {
                return chess.indexOf(i);
            }
        }
        return -1;
    }

    /**
     * 卡牌战斗核心逻辑
     *
     * @param Acards A的战场卡牌
     * @param Bcards B的战场卡牌
     * @param pos 卡牌位置
     */
    private void findChessAndFight(ArrayList<Chess> Acards, ArrayList<Chess> Bcards, int pos) {
        //获得所在位置的双方卡牌
        Chess A = Acards.get(pos);
        Chess B = Bcards.get(pos);
        //如果双方卡牌都存活则发生战斗
        if (A.isAlive() && B.isAlive()) {
            cardFight(A, B);
            //如果A存活但B不存活则寻找B的左边和右边是否还有存活卡牌，如果有则进入战斗
        } else if (A.isAlive() && !B.isAlive()) {
            int finder = findOtherChess(Bcards, pos);
            if (finder != -1) {
                cardFight(A, Bcards.get(finder));
            }
            //如果B存活但A不存活则寻找A的左边和右边是否还有存活卡牌，如果有则进入战斗
        } else if (!A.isAlive() && B.isAlive()) {
            int finder = findOtherChess(Acards, pos);
            if (finder != -1) {
                cardFight(B, Acards.get(finder));
            }
        }
    }

    /**
     * 检测玩家是否场上还有存货的卡牌
     *
     * @param who 1 表示玩家1、2 表示玩家2
     * @return 仍有存活卡牌返回true否则返回false
     */
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

    /**
     * 战斗结算
     */
    private void battleResult() {
        if (noCardsToBattle(1)) {
            //玩家1场上已经没有存活卡牌
            //则清点玩家2场上卡牌数量
            //根据数量扣除玩家2  卡牌数量*2 的血量
            //同时将玩家2连胜标识+1 将玩家1连胜标识清零
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
        } else if (noCardsToBattle(2)) {
            //玩家2场上没有存活卡牌时处理逻辑与玩家1类似
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

    /**
     * 整场游戏结束检测
     * 任一玩家血量降至0以下则该局游戏结束
     */
    private void battleFinishCheck() {
        if (playerOne.getHp() <= 0 || playerTwo.getHp() <= 0) {
            state = GameState.FINISHED;
        } else {
            state = GameState.PREPARE;
        }
    }

    /**
     * 刷新金钱
     * 每回合结束结算玩家金钱
     * 玩家可以获得基于当前剩余金钱数计算得到的利息
     *
     * 当玩家拥有10金币，则获得1金币的利息
     * 当玩家拥有20金币，则获得2金币的利息
     * 以此类推直至50,50以上的金币则只给5金币
     * 由于当前游戏卡牌设定购买价格过高，故玩家将会获得基于以上规则金币的两倍金币
     */
    private void refeashGold() {
        if (playerOne.getGold() < 50) {
            int extraGold = playerOne.getGold() / 10 * 2;
            LOG.info(String.format("###### 玩家 %s 本轮游戏获得 %d 金币 ######", playerOne.getName(), extraGold));
            playerOne.setGold(playerOne.getGold() + extraGold);
        } else {
            LOG.info(String.format("###### 玩家 %s 本轮游戏获得 10 金币 ######", playerOne.getName()));
            playerOne.setGold(playerOne.getGold() + 10);
        }
        LOG.info(String.format("###### 玩家 %s 当前总金币数： %d ######", playerOne.getName(), playerOne.getGold()));

        if (playerTwo.getGold() < 50) {
            int extraGold = playerTwo.getGold() / 10 * 2;
            LOG.info(String.format("###### 玩家 %s 本轮游戏获得 %d 金币 ######", playerTwo.getName(), extraGold));
            playerTwo.setGold(playerTwo.getGold() + extraGold);
        } else {
            LOG.info(String.format("###### 玩家 %s 本轮游戏获得 10 金币 ######", playerTwo.getName()));
            playerTwo.setGold(playerTwo.getGold() + 10);
        }
        LOG.info(String.format("###### 玩家 %s 当前总金币数： %d ######", playerOne.getName(), playerTwo.getGold()));
    }

    //双方玩家战场上卡牌的战斗处理整体逻辑
    public void fight() {
        //获得双方战场卡牌
        ArrayList<Chess> chessOne = playerOne.getBattleCards();
        ArrayList<Chess> chessTwo = playerTwo.getBattleCards();
        Random random = new Random();
        //任何一方场上无存货卡牌则停止循环
        while (noCardsToBattle(1) && noCardsToBattle(2)) {
            //如果玩家1的战场牌数比玩家2的多，则以玩家1的牌数为循环基准
            //否则相反
            if (chessOne.size() >= chessTwo.size()) {
                //从第一张牌开始一直遍历到与对方卡牌站位对齐的最后一张卡牌
                for (int i = 0; i < chessOne.size(); ++i) {
                    if (i < chessTwo.size()) {
                        //以当前卡牌位置寻找对手并处理战斗
                        findChessAndFight(chessOne, chessTwo, i);
                    } else {
                        //其余没有对位卡牌的牌则额外寻找场上其他是否还有存货的牌并战斗
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
        LOG.info("###### 回合 " + this.rounds + " 计算结束 开始结算过程 ######");
        battleResult();
        refeashGold();
        battleFinishCheck();
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

    public Date getCalEndDT() {
        return calEndDT;
    }

    public void setCalEndDT(Date calEndDT) {
        this.calEndDT = calEndDT;
    }

}

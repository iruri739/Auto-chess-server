package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.business.ChessManager;
import com.accenture.huaweigroup.business.ResManager;
import com.accenture.huaweigroup.module.entity.*;
import com.accenture.huaweigroup.module.bean.*;
import com.accenture.huaweigroup.module.mapper.GameRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private GameRecordMapper gameRecordMapper;
    @Autowired
    private ChessManager chessManager;
    @Autowired
    private UserService userService;

//    //玩家匹配列表
//    private ConcurrentHashMap<Integer, Boolean> waitPlayerList = new ConcurrentHashMap<>();
//    //游戏列表
//    private ConcurrentHashMap<Integer, Game> gameList = new ConcurrentHashMap<>();

    /**
     * 匹配玩家机制
     * @param playerId 玩家id
     * @return 成功返回 ture 否则返回 false
     */
    public boolean matchGame(int playerId) throws Exception {
        ResManager.showMatchList();
        if (ResManager.findGameByPlayer(playerId) != null) {
            return true;
        }
        //如果玩家已经在列表则继续等待
        if (ResManager.isMatching(playerId)) {
            LOG.info("玩家 " + playerId + " 已在匹配列表中，继续等待");
            return false;
        }
        //如果玩家不在列表，检查列表是否为空，为空添加到列表中
        //列表不为空则寻找尚未完成匹配准备的玩家并创建游戏等待玩家准备
        if (ResManager.matchListSize() == 0) {
            LOG.info("匹配列表中无玩家，将玩家 " + playerId + " 加入列表");
            ResManager.joinMatch(playerId);
            return false;
        } else {
            LOG.info("当前列表已有玩家，寻找对手");
            int opponentId = ResManager.findMatch(playerId);
            if (opponentId != 0) {
                createGame(playerId, opponentId);
                ResManager.delFromMatch(playerId);
                ResManager.delFromMatch(opponentId);
                return true;
            }
        }
        return false;
    }

    /**
     * 创建游戏
     * @param playerOneId 玩家1的ID
     * @param playerTwoId 玩家2的ID
     */
    public void createGame(int playerOneId, int playerTwoId) {
        String uuid = UUID.randomUUID().toString().substring(24);
        Game newGame = new Game(uuid, playerOneId, playerTwoId);
        newGame.getPlayerOne().setCardInventory((ArrayList<Chess>) chessManager.getRandomChess());
        newGame.getPlayerOne().setName(userService.getUserById(playerOneId).getName());
        newGame.getPlayerTwo().setCardInventory((ArrayList<Chess>) chessManager.getRandomChess());
        newGame.getPlayerTwo().setName(userService.getUserById(playerTwoId).getName());
        ResManager.addToGameList(newGame);
    }

    /**
     * 检查玩家是否可以进入游戏准备阶段
     * @param gameId 游戏ID
     * @param playerId 玩家ID
     * @return
     */
    public String gameGoPrepareCheck(String gameId, int playerId) {
        Game game = ResManager.findGameById(gameId);
        Player player = game.getPlayer(playerId);
        if (game.getState() == GameState.FINISHED) {
            if (player.getHp() > 0) {
                return "win";
            } else {
                return "lose";
            }
        }
        if (player.getState() != PlayerState.PREPARE) {
            player.setState(PlayerState.PREPARE);
            return "false";
        } else {
            if (game.checkPlayerState(PlayerState.PREPARE)) {
                game.setState(GameState.PREPARE);
                return "true";
            }
        }
        return "false";
    }

    public boolean buyNewCards(String gameId, int playerId, ArrayList<Chess> newCards) throws Exception {
        Game game = ResManager.findGameById(gameId);
        Player player = game.getPlayer(playerId);
        if (newCards == null) {
            newCards = new ArrayList<>();
        }
        for (Chess chess : newCards) {
            player.setGold(player.getGold() - chess.getPrice());
        }
        player.getHandCards().addAll(newCards);
        LOG.info("###### 玩家 "+ player.getName() +" 添加新卡牌");
        LOG.info(newCards.toString());
        return true;
    }

    public PlayerBattleData sendBattleData(String gameId) throws Exception {
        Game game = ResManager.findGameById(gameId);
        PlayerBattleData data = new PlayerBattleData();
        data.setPlayerOneId(game.getPlayerOne().getId());
        data.setPlayerOneBattleCards(game.getPlayerOne().getBattleCards());
        data.setPlayerTwoId(game.getPlayerTwo().getId());
        data.setPlayerTwoBattleCards(game.getPlayerTwo().getBattleCards());
        return data;
    }

    /**
     * UpdateGameData包含游戏id、玩家id、战场的卡牌id列表
     * 根据以上信息更新某一场游戏某位玩家的卡牌信息
     *
     * @param data 前端传来的信息
     * @return 成功返回true，否则报错
     */
    public boolean battleDataApi(UpdateGameData data) throws Exception {
        Game game = ResManager.findGameById(data.getGameId());
        if (data.getCards() == null) {
            data.setCards(new ArrayList<>());
        }
        game.setPlayerBattleCards(data.getPlayerId(),(ArrayList<Chess>) data.getCards());
        return true;
    }

    /**
     * 检查玩家是否可以进入战斗状态
     * @param gameId
     * @param playerId
     * @return
     */
    public boolean gameBattleCheck(String gameId, int playerId) {
        Game game = ResManager.findGameById(gameId);
        Player player = game.getPlayer(playerId);
        if (player.getState() != PlayerState.BATTLE) {
            player.setState(PlayerState.BATTLE);
            return false;
        } else {
            if (game.checkPlayerState(PlayerState.BATTLE)) {
                game.setState(GameState.BATTLE);
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新玩家待选区卡牌，金币不够或发生错误则返回空数组
     * @param gameId
     * @param playerId
     * @return
     */
    public ArrayList<Chess> changePlayerInventory(String gameId, int playerId) {
        Game game = ResManager.findGameById(gameId);
        Player player = game.getPlayer(playerId);
        if (player.getGold() >= 2) {
            player.setGold(player.getGold() - 2);
            ArrayList<Chess> newInventory = (ArrayList<Chess>) chessManager.getRandomChess();
            player.setCardInventory(newInventory);
            return newInventory;
        }
        return new ArrayList<>();
    }

    //获取游戏初始数据
    public BattleData getInitGameData(int playerId) {
        Game game = ResManager.findGameByPlayer(playerId);
        game.getPlayerOne().setCardInventory((ArrayList<Chess>) chessManager.getRandomChess());
        game.getPlayerTwo().setCardInventory((ArrayList<Chess>) chessManager.getRandomChess());
        BattleData data = new BattleData(game);
        return data;
    }




    //掉线重连
    public boolean checkGameState(int playerId) {
        Game game = ResManager.findGameByPlayer(playerId);
        if (game != null) {
            return true;
        } else {
            return false;
        }
    }


}

package com.accenture.huaweigroup.service;

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
    private ChessService chessService;
    @Autowired
    private UserService userService;

    //玩家匹配列表
    private ConcurrentHashMap<Integer, Boolean> waitPlayerList = new ConcurrentHashMap<>();
    //游戏列表
    private ConcurrentHashMap<Integer, Game> gameList = new ConcurrentHashMap<>();

    /**
     * 匹配玩家机制
     * @param playerId 玩家id
     * @return 成功返回 ture 否则返回 false
     */
    public boolean matchGame(int playerId) throws Exception {
        LOG.info("当前玩家列表：");
        LOG.info(waitPlayerList.toString());
        //如果玩家已经在列表则检查是否已经完成匹配准备
        if (ResManager.isMatching(playerId)) {
            if (ResManager.getMatchState(playerId)) {
                LOG.info("玩家 " + playerId + " 已在列表中，且已经准备开始游戏");
                return true;
            } else {
                LOG.info("玩家 " + playerId + " 已在列表中，但仍未准备");
                return false;
            }
        }
        //如果玩家不在列表，检查列表是否为空，为空添加到列表中
        //列表不为空则寻找尚未完成匹配准备的玩家并创建游戏等待玩家准备
        if (ResManager.getWaitListSize() == 0) {
            LOG.info("匹配列表中无玩家，将玩家 " + playerId + " 加入列表");
            ResManager.waitMatch(playerId);
            return false;
        } else {
            LOG.info("当前列表已有玩家，寻找对手");
            ResManager.waitMatch(playerId);//2019年9月5日14:13:31
            int opponentId = ResManager.findOpponent(playerId);
            if (opponentId != 0) {
                createGame(playerId, opponentId);
                return true;
            }
//            Iterator<Map.Entry<Integer, Boolean>> iterator = waitPlayerList.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<Integer, Boolean> entry = iterator.next();
//                if (entry.getKey() != playerId && !entry.getValue()) {
//                    LOG.info("找到对手！ID为 " + entry.getKey() + " 准备创建游戏！");
//                    createGame(playerId, entry.getKey());
//                    waitPlayerList.replace(entry.getKey(), true);
//                    waitPlayerList.replace(playerId, true);
//                    break;
//                }
//            }
        }
        return false;
    }

    /**
     * 根据用户id寻找游戏id
     * @param playerId
     * @return
     */
    private Game findGameByPlayerId(int playerId) {
        for (Game game : gameList.values()) {
            if (game.containPlayer(playerId)) {
                return game;
            }
        }
        return null;
    }

    /**
     *  检查双方玩家是否已经做好匹配准备
     * @param playerId 玩家ID
     * @return
     */
    public boolean matchReadyCheck(int playerId) {
        Game game = findGameByPlayerId(playerId);
        if (game == null) {
            return false;
        }
        if (game.getPlayer(playerId).getState() == PlayerState.NONE) {
            game.getPlayer(playerId).setState(PlayerState.REDAY);
            return false;
        } else {
            if (game.checkPlayerState(PlayerState.REDAY)) {
                waitPlayerList.remove(playerId);
//                GameRecord record = gameRecordMapper.
                return true;
            }
        }
        return false;
    }

    /**
     * 取消匹配流程
     *
     * @param playerId
     * @return
     * @throws Exception
     */
    public boolean cancelMatch(int playerId) throws Exception {
        if (waitPlayerList.containsKey(playerId)) {
            waitPlayerList.remove(playerId);
            return true;
        }
        return false;
    }

    /**
     * 创建游戏
     * @param playerOneId 玩家1的ID
     * @param playerTwoId 玩家2的ID
     */
    public void createGame(int playerOneId, int playerTwoId) {
        gameRecordMapper.insert(new GameRecord(playerOneId, playerTwoId));
        GameRecord gameRecord = gameRecordMapper.findByPlayerId(playerOneId, playerTwoId);
        Game newGame = new Game(gameRecord.getRecordId(), playerOneId, playerTwoId);
        newGame.getPlayerOne().setCardInventory(chessService.getRandomCards());
        newGame.getPlayerOne().setName(userService.getUserById(playerOneId).getName());
        newGame.getPlayerTwo().setCardInventory(chessService.getRandomCards());
        newGame.getPlayerTwo().setName(userService.getUserById(playerTwoId).getName());
        gameList.put(newGame.getId(), newGame);
    }

    /**
     * 检查玩家是否可以进入游戏准备阶段
     * @param gameId 游戏ID
     * @param playerId 玩家ID
     * @return
     */
    public boolean gamePrepareCheck(int gameId, int playerId) {
        Game game = gameList.get(gameId);
        Player player = game.getPlayer(playerId);
        if (player.getState() != PlayerState.PREPARE) {
            player.setState(PlayerState.PREPARE);
            return false;
        } else {
            if (game.checkPlayerState(PlayerState.PREPARE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新玩家待选区卡牌，金币不够或发生错误则返回null
     * @param gameId
     * @param playerId
     * @return
     */
    public ArrayList<Chess> changePlayerInventory(int gameId, int playerId) {
        Game game = gameList.get(gameId);
        Player player = game.getPlayer(playerId);
        if (player.getGold() >= 2) {
            player.setGold(player.getGold() - 2);
            ArrayList<Chess> newInventory = chessService.getRandomCards();
            player.setCardInventory(newInventory);
            return newInventory;
        }
        return null;
    }

    //获取游戏初始数据
    public BattleData getInitGameData(int playerId) {
        Game game = findGameByPlayerId(playerId);
        game.getPlayerOne().setCardInventory(chessService.getRandomCards());
        game.getPlayerTwo().setCardInventory(chessService.getRandomCards());
        BattleData data = new BattleData(game);
        return data;
    }

    //获取并更新玩家数据
    public BattleData battleDataApi(BattleData data) {
        Game game = gameList.get(data.getGameId());
        game.refreshData(data);
        return new BattleData(game);
    }

    public String checkGameState(BattleData data) {
        return null;
    }

    public String checkBattleState(BattleData data) {
        if (data.getState() == 0) {
            Game game = gameList.get(data.getGameId());
            game.setRounds(game.getRounds() + 1);

        }
        return null;
    }

    //计时轮训gamelist清除异常游戏并做异常处理
//    @Scheduled(initialDelay = 1000, fixedRate = 60*1000)
//    private void cycleCheckGame() {
//
//    }


}

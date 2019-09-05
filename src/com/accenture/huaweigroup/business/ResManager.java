package com.accenture.huaweigroup.business;

import com.accenture.huaweigroup.module.bean.Game;
import com.accenture.huaweigroup.module.mapper.GameRecordMapper;
import com.accenture.huaweigroup.module.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResManager {
    private static final Logger LOG = LoggerFactory.getLogger(ResManager.class);

    //用户在线列表
    private static ConcurrentHashMap<Integer, Boolean> userOnlineList = new ConcurrentHashMap<>();

    public static void addUserToList(int userId, boolean state) {
        userOnlineList.put(userId, state);
    }

    public static Boolean removeUserFromList(int userId) {
        return userOnlineList.remove(userId);
    }

    public static boolean getUserState(int userId) {
        return userOnlineList.get(userId);
    }

    public static void changeUserState(int userId, boolean state) {
        userOnlineList.replace(userId, state);
    }

    public static List<Integer> getOnlineUserList() {
        List<Integer> list = new ArrayList<>();
        Iterator<Map.Entry<Integer, Boolean>> iterator = userOnlineList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = iterator.next();
            if (entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    /**
     * Spring Task 计时任务，由Spring boot自动执行
     * 每隔5分钟轮询检查在线列表
     * 将所有在线状态（true）的用户调整为即将离线状态（false）
     * 将所有即将离线状态的用户从列表清除
     */
    @Scheduled(initialDelay = 5*60*1000, fixedDelay = 5*60*1000)
    private static void checkOnlineTimeTask() {
        LOG.info("当前用户在线列表：" + userOnlineList);
        Iterator<Map.Entry<Integer, Boolean>> iterator = userOnlineList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = iterator.next();
            if (entry.getValue()) {
                userOnlineList.replace(entry.getKey(), false);
            } else {
                iterator.remove();
            }
        }
    }

    //玩家匹配列表
    private static ConcurrentHashMap<Integer, Boolean> waitPlayerList = new ConcurrentHashMap<>();

    public static boolean isMatching(int playerId) {
        return waitPlayerList.containsKey(playerId);
    }

    public static boolean getMatchState(int playerId) {
        return waitPlayerList.get(playerId);
    }

    public static void showMatchList() {
        LOG.info("当前玩家列表：");
        LOG.info(waitPlayerList.toString());
    }

    public static void joinMatch(int playerId) {
        waitPlayerList.put(playerId, false);
    }

    public static void matchFinish(int playerId) {
        waitPlayerList.remove(playerId);
    }

    public static void cancelMatch(int playerId) {
        matchFinish(playerId);
    }

    public static void changeMatchState(int playerId, boolean state) {
        waitPlayerList.replace(playerId, state);
    }

    public static int findOpponent(int playerId) {
        Iterator<Map.Entry<Integer, Boolean>> iterator = waitPlayerList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = iterator.next();
            if (entry.getKey() != playerId && !entry.getValue()) {
                LOG.info("找到对手！ID为 " + entry.getKey() + " 准备创建游戏！");
                waitPlayerList.replace(entry.getKey(), true);
                waitPlayerList.replace(playerId, true);
                return entry.getKey();
            }
        }
        return 0;
    }

    public static int getWaitListSize() {
        return waitPlayerList.size();
    }

    //游戏列表
    private static ConcurrentHashMap<Integer, Game> gameList = new ConcurrentHashMap<>();

    public static Game findGameByPlayer(int playerId) {
        Iterator<Map.Entry<Integer, Game>> iterator = gameList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Game> entry = iterator.next();
            if (entry.getValue().getPlayer(playerId) != null) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Game findGameById(int gameId) {
        return gameList.get(gameId);
    }

    public static void addToGameList(Game game) {
        gameList.put(game.getId(), game);
    }

}

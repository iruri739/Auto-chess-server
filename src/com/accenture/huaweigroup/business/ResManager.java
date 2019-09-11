package com.accenture.huaweigroup.business;

import com.accenture.huaweigroup.module.bean.Game;
import com.accenture.huaweigroup.module.bean.GameState;
import com.accenture.huaweigroup.module.mapper.GameRecordMapper;
import com.accenture.huaweigroup.module.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

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
    @Scheduled(initialDelay = 3*60*1000, fixedDelay = 3*60*1000)
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
    private static ConcurrentSkipListSet<Integer> matchList = new ConcurrentSkipListSet<>();


    public static void showMatchList() {
        LOG.info("当前玩家匹配列表：");
        LOG.info(matchList.toString());
    }

    public static boolean isMatching(int playerId) {
        if (matchList.contains(playerId)){
            return true;
        }
        return false;
    }

    public static void joinMatch(int playerId) {
        matchList.add(playerId);
    }

    public static void delFromMatch(int playerId) {
        matchList.remove(playerId);
    }

    public static int findMatch(int playerId) {
        for (int id : matchList) {
            if (id != playerId) {
                return id;
            }
        }
        return 0;
    }

    public static int matchListSize() {
        return matchList.size();
    }



    //游戏列表
    private static ConcurrentHashMap<String, Game> gameList = new ConcurrentHashMap<>();

    //检查游戏列表是否存在异常游戏并清除异常游戏
    @Scheduled(initialDelay = 1000, fixedDelay = 5*60*1000)
    private void gameCircleCheck() {
//        Iterator<>
    }

    public static Game findGameByPlayer(int playerId) {
        Iterator<Map.Entry<String, Game>> iterator = gameList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Game> entry = iterator.next();
            if (entry.getValue().getPlayer(playerId) != null) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static void clearAllGame() {
        gameList.clear();
    }

    public static Game findGameById(String gameId) {
        return gameList.get(gameId);
    }

    public static void addToGameList(Game game) {

        gameList.put(game.getId(), game);
    }

}

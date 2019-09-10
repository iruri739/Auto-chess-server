package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.module.bean.PlayerBattleData;
import com.accenture.huaweigroup.module.bean.UpdateGameData;
import com.accenture.huaweigroup.module.entity.*;
import com.accenture.huaweigroup.module.bean.BattleData;

import com.accenture.huaweigroup.module.exception.NoGameException;
import com.accenture.huaweigroup.module.exception.NoPlayerException;
import com.accenture.huaweigroup.service.GameService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/game")
@Api(value = "游戏逻辑", tags = "游戏逻辑接口")
public class GameController {

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @ApiOperation(value = "玩家匹配", notes = "玩家调用接口，返回true表明匹配成功，false表明仍在匹配", httpMethod = "GET")
    @GetMapping("/matchGame")
    public boolean matchGame(@RequestParam("playerId") int playerId) {
        try {
            return gameService.matchGame(playerId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("玩家[" + playerId +"] 匹配过程发生错误！！！");
        }
        return false;
    }

    @ApiOperation(value = "获取当局游戏数据对象", notes = "通过调用该接口获得数据对象", httpMethod = "GET")
    @GetMapping("/initRounds")
    public BattleData initRounds(@RequestParam("playerId") int playerId, HttpServletResponse response) {
        try {
            return gameService.initRounds(playerId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(String.format("###### 玩家 %d 获取游戏对象发生错误！！！ ######", playerId));
        }
        return null;
    }

//    @ApiOperation(value = "断线重连检测", notes = "检查用户是否已经在游戏中，如果是则返回true，否则返回false", httpMethod = "GET")
//    @GetMapping("/checkGameState")
//    public boolean disconnectCheck(@RequestParam("userId") int userId) {
//        return gameService.checkGameState(userId);
//    }

    @ApiOperation(value = "获取刷新待选区卡牌列表", notes = "扣除玩家2金币刷新待选区卡牌，无法刷新则返回null", httpMethod = "GET")
    @GetMapping(value = "/getChessData")
    public ArrayList<Chess> getChessData(@RequestParam("gameId") String gameId,
                                         @RequestParam("playerId") int playerId)
    {
        try {
            return gameService.changePlayerInventory(gameId, playerId);
        } catch (NoGameException | NoPlayerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "上传新购买卡牌数据", notes = "附带参数玩家ID、游戏ID、卡牌对象数组", httpMethod = "POST")
    @PostMapping("/updateHandCards")
    public boolean updateHandCards(@RequestBody UpdateGameData data) {
        try {
            return gameService.buyNewCards(data.getGameId(), data.getPlayerId(), (ArrayList<Chess>) data.getCards());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @ApiOperation(value = "对方战场数据获取接口", notes = "向服务器发送自己的ID 获取对方的战场数据", httpMethod = "GET")
    @PostMapping("/requestBattleData")
    public BattleData requestBattleData(@RequestParam("playerId") int playerId,
                                              @RequestParam("round") int round) {
        try {
            return gameService.sendCacheData(playerId, round);
        } catch (NoGameException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "战场数据传输接口", notes = "向服务器发送json对象，返回服务器最新状态的json对象", httpMethod = "POST")
    @PostMapping("/sendBattleData")
    public boolean sendBattleData(@RequestBody UpdateGameData data) {
        try {
            return gameService.battleDataApi(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

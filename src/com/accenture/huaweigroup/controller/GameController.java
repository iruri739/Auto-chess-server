package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.module.bean.PlayerBattleData;
import com.accenture.huaweigroup.module.bean.UpdateGameData;
import com.accenture.huaweigroup.module.entity.*;
import com.accenture.huaweigroup.module.bean.BattleData;

import com.accenture.huaweigroup.service.GameService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    @ApiOperation(value = "断线重连检测", notes = "检查用户是否已经在游戏中，如果是则返回true，否则返回false", httpMethod = "GET")
//    @GetMapping("/checkGameState")
//    public boolean disconnectCheck(@RequestParam("userId") int userId) {
//        return gameService.checkGameState(userId);
//    }

    @ApiOperation(value = "获取初始化数据接口", notes = "通过调用该接口获得初始json数据对象", httpMethod = "GET")
    @GetMapping("/defaultDataModel")
    public BattleData sendDefaultDataModel(@RequestParam("playerId") int playerId) {
        return gameService.getInitGameData(playerId);
    }

    @ApiOperation(value = "进入准备阶段检测", notes = "检测玩家是否准备开启战斗", httpMethod = "GET")
    @GetMapping("/gamePrepareCheck")
    public String gameCircleCheck(@RequestParam("gameId") String gameId,@RequestParam("playerId") int playerId) {
        return gameService.gameGoPrepareCheck(gameId, playerId);
    }

    @ApiOperation(value = "进入战斗阶段检测", notes = "检测玩家是否可以进入战斗阶段", httpMethod = "GET")
    @GetMapping("/gameBattleCheck")
    public boolean gameBattleCheck(@RequestParam("gameId") String gameId, @RequestParam("playerId") int playerId) {
        return gameService.gameBattleCheck(gameId, playerId);
    }

    @ApiOperation(value = "获取刷新待选区卡牌列表", notes = "扣除玩家2金币刷新待选区卡牌，无法刷新则返回null", httpMethod = "GET")
    @GetMapping(value = "/getChessData")
    public ArrayList<Chess> getChessData(@RequestParam("gameId") String gameId,
                                         @RequestParam("playerId") int playerId)
    {
        return gameService.changePlayerInventory(gameId, playerId);
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

    @PostMapping("/downloadBattleData")
    public PlayerBattleData sendBattleData(String gameId) {
        try {
            return gameService.sendBattleData(gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "战场数据传输接口", notes = "向服务器发送json对象，返回服务器最新状态的json对象", httpMethod = "POST")
    @PostMapping("/battleDataApi")
    public boolean sendBattleData(@RequestBody UpdateGameData data) {
        try {
            return gameService.battleDataApi(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

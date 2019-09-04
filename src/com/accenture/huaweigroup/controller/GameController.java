package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.entity.Game;
import com.accenture.huaweigroup.module.entity.GameInitData;
import com.accenture.huaweigroup.module.entity.PlayerData;
import com.accenture.huaweigroup.service.ChessService;

import com.accenture.huaweigroup.service.GameService;
import com.accenture.huaweigroup.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/game")
@Api(value = "游戏逻辑", tags = "游戏逻辑接口")
public class GameController {

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private ChessService chessService;

    @ApiOperation(value = "玩家首次匹配", notes = "调用该接口", httpMethod = "GET")
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

    @ApiOperation(value = "验证玩家匹配", notes = "验证匹配的玩家是否都已经准备开始游戏", httpMethod = "GET")
    @GetMapping("/checkMatch")
    public boolean checkMatch(@RequestParam("playerId") int playerId) {
        if (gameService.matchReadyCheck(playerId)) {
//            Game game = gameService.findGameByPlayerId(playerId);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "取消首次匹配", notes = "取消玩家匹配过程接口", httpMethod = "GET")
    @GetMapping("/cancelMatch")
    public boolean cancelMatch(@RequestParam("playerId") int playerId) {
        try {
            if (gameService.cancelMatch(playerId)) {
                LOG.error("玩家[" + playerId +"] 取消匹配过程成功！");
                return true;
            } else {
                LOG.error("玩家[" + playerId +"] 取消匹配过程失败！该玩家未开始匹配或已经进入游戏！");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("玩家[" + playerId +"] 取消匹配过程发生错误！！！");
        }
        return false;
    }

    @ApiOperation(value = "获取初始游戏数据", notes = "调用接口获取初始玩家游戏数据", httpMethod = "GET")
    @GetMapping("/getInitData")
    public GameInitData getGameInitData(@RequestParam("playerId") int playerId) {
        return gameService.getInitGameData(playerId);
    }

    @ApiOperation(value = "玩家准备检测", notes = "检测玩家是否准备开启战斗", httpMethod = "GET")
    @GetMapping("/gameStartCheck")
    public boolean gameStartCheck(int gameId, int playerId) {
        return gameService.gameStartCheck(gameId, playerId);
    }

    @ApiOperation(value = "获取刷新待选区卡牌列表", notes = "返回", httpMethod = "GET")
    @GetMapping(value = "/getChessData")
    public ArrayList<Chess> getChessData(@RequestParam("gameId") int gameId,
                                         @RequestParam("playerId") int playerId)
    {
        return chessService.getRandomCards();
    }

    @GetMapping("/checkGameResult")
    public String checkGameResult(@RequestParam("playerId") int playerId) {
        return null;
    }

	
}

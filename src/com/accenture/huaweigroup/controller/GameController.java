package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.module.entity.Chess;
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


    @GetMapping(value = "/getPlayerData")
    public String getPlayerData(@RequestParam("playerId") int playerId){
        String playerData = gameService.getPlayerDate(playerId).toString();
        return  playerData;
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

    @ApiOperation(value = "玩家匹配", notes = "玩家匹配接口", httpMethod = "GET")
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

    @ApiOperation(value = "取消匹配", notes = "取消玩家匹配过程接口", httpMethod = "GET")
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
//	@Autowired
//	private GameService gameService;
//
//	@GetMapping(value = "/create")
//	public void buyAnimal(@RequestParam("firstUserID") int firstUserID,@RequestParam("secondUserID") int secondUserID) {
//
//	}
//
//	@GetMapping(value = "/join")
//	public String joinGame(@RequestParam("userID") Integer userId) {
//		if (gameService.join(userId) == 1) {
//			return "等待";
//		} else {
//			return "成功";
//		}
//	}
//
//	@GetMapping(value = "/refresh")
//	public set<Animal> refreshAnimal() {
//
//		return null;
//
//	}
//
//	@GetMapping(value = "/fight")
//	public Integer fightAniaml(@RequestParam("firstAnimalSet") set<Animal> firstAnimalSet,@RequestParam("secondAnimalSet") set<Animal> secondAnimalSet) {
//
//		return 0;
//
//	}
//
//	@GetMapping(value = "/match")
//	public boolean matchGame(@RequestParam("userID") Integer userID) {
//
//		return true;
//
//	}
//
//
	
}

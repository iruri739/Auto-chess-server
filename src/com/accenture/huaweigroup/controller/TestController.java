package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.business.ResManager;
import com.accenture.huaweigroup.module.bean.Game;
import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.service.GameService;
import com.accenture.huaweigroup.service.GameThreadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping("/test")
@Api(value = "测试接口", tags = "测试调用接口用接口")
public class TestController {

    @Autowired
    GameService gameService;

    @GetMapping("/createGame")
    public String testCreateGame(@RequestParam("playerOneId") int playerOneId,
                                 @RequestParam("playerTwoId") int playerTwoId) {
        gameService.createGame(playerOneId, playerTwoId);
        return ResManager.findGameByPlayer(playerOneId).getId();
    }

    @GetMapping("/closeGame")
    public void closeGame() {
        GameThreadService.clear();
        ResManager.clearAllGame();
    }

    @GetMapping("/changeBattle")
    public String testChangeBattleCards(@RequestParam("playerId") int playerOneId, HttpServletRequest request) {
        Game game = ResManager.findGameByPlayer(playerOneId);
        ArrayList<Chess> handCards = game.getPlayerHandCards(playerOneId);
        ArrayList<Chess> cardInventory = game.getPlayerCardInventory(playerOneId);
        ArrayList<Chess> battleCards = game.getPlayerBattleCards(playerOneId);
        Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            int rand = random.nextInt(cardInventory.size());
            battleCards.add(cardInventory.get(rand));
        }


        return "true";
    }

}

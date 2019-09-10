package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.business.ResManager;
import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.service.GameService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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

}

package com.accenture.huaweigroup.test.service;

import com.accenture.huaweigroup.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {
    @Autowired
    GameService gameService;

    @Test
    public void gameRecordTest() {
        gameService.createGame(1,2);
    }
}

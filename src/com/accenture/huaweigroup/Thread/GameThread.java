package com.accenture.huaweigroup.Thread;

import com.accenture.huaweigroup.business.ResManager;
import com.accenture.huaweigroup.module.bean.Game;
import com.accenture.huaweigroup.module.bean.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class GameThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(GameThread.class);

    private String gameId;
    public GameThread(String gameId)
    {
        this.gameId = gameId;
    }

    public void run() {
        try {
            while(true)
            {
                LOG.info(String.format("###### 游戏 %s 开始计时 ######", gameId));
                Thread.sleep(Game.PLAYER_DEFAULT_PREPARETIME);
                //通过参数gameId从全局map中获取游戏数据
                Game game = ResManager.findGameById(this.gameId);
                //计算战斗
                LOG.info(String.format("###### 游戏 %s 开始战斗计算 ######", gameId));
                game.fight();
                LOG.info(String.format("###### 游戏 %s 战斗计算结束 ######", gameId));
                if (game.getState() == GameState.FINISHED) {
                    LOG.info(String.format("###### 游戏 %s 游戏结束 ######", gameId));
                    break;
                }
                //更新game中战斗结束系统时间
                game.setCalEndDT(new Date());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

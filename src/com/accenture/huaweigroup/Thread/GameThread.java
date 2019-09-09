package com.accenture.huaweigroup.Thread;

import com.accenture.huaweigroup.business.ResManager;
import com.accenture.huaweigroup.module.bean.Game;

import java.util.Date;

public class GameThread implements Runnable{

    private String gameId;
    public GameThread(String gameId)
    {
        this.gameId = gameId;
    }

    public void run() {
        try {
            while(true)
            {
                //通过参数gameId从全局map中获取游戏数据
                Game game = ResManager.findGameById(this.gameId);
                //计算战斗

                //更新game中战斗结束系统时间
                game.setCalEndDT(new Date());
                //sleep
                Thread.sleep(Game.PLAYER_DEFAULT_PREPARETIME);
            }
        }catch (Exception e){

        }




    }


}

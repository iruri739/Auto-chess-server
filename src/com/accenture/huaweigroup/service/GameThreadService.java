package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.Thread.GameThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameThreadService {

    public void run(String gameId){
        ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(new GameThread(gameId));


    }
}

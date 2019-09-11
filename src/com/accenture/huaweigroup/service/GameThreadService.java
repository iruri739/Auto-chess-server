package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.Thread.GameThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameThreadService {

    private static ExecutorService exec = Executors.newCachedThreadPool();

    public static void run(String gameId){
        exec.execute(new GameThread(gameId));
    }

    public static void clear() {
        exec.shutdown();
    }
}

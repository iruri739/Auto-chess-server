package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.mapper.ChessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//当前版本被ChessManager类替代，保留该类副本以备后用

//@Service
public class ChessService implements InitializingBean {

//    @Autowired
//    private ChessMapper chessMapper;
//
//    private static final Logger LOG = LoggerFactory.getLogger(ChessService.class);
//
//    private static ConcurrentHashMap<Integer, Chess> chessMap = new ConcurrentHashMap<>();
//
//    public ChessService() {
//        super();
//    }
//
//    //生成指定卡牌id的卡牌对象副本
//    public Chess createChess(int chessId) {
//        return new Chess(chessMap.get(chessId));
//    }
//
//    //获取固定数量（5）的卡牌集合
//    public ArrayList<Chess> getRandomCards() {
//        Random random = new Random();
//        ArrayList<Chess> cards = new ArrayList<>();
//        for (int i = 0; i < 5; ++i) {
//            int cardId = 0;
//            cardId = random.nextInt(chessMap.size()) + 1;
//            cards.add(chessMap.get(cardId));
//        }
//        return cards;
//    }
//
//    //获取指定数量的卡牌集合
//    public ArrayList<Chess> getRandomCards(int count) {
//        Random random = new Random();
//        ArrayList<Chess> cards = new ArrayList<>();
//        for (int i = 0; i < count; ++i) {
//            int cardId = 0;
//            cardId = random.nextInt(chessMap.size()) + 1;
//            cards.add(chessMap.get(cardId));
//        }
//        return cards;
//    }
//
//    //此方法为javaBean初始化方法之一，仅执行一次，用于服务器启动时
//    //从数据库读取卡牌列表数据至内存中
//    //保存至成员变量chessMap中
//    @Override
    public void afterPropertiesSet() throws Exception {
//        List<Chess> list = chessMapper.getAll();
//        for (Chess c : list) {
//            chessMap.put(c.getId(), c);
//        }
//        LOG.info("ChessMap initialize finished!!");
//        LOG.info(list.toString());
    }
}

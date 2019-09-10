package com.accenture.huaweigroup.business;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.mapper.ChessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ChessManager implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(ChessManager.class);
    private static final String CHESS_DB = "CHESS_DB_";
    private int chessSize;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ChessMapper chessMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("###### 正在加载牌库数据至 Redis 数据库中 ######");
        List<Chess> chessList = chessMapper.getAll();
        chessSize = chessList.size();
        LOG.info("###### 成功从数据库读取 " + chessSize + " 张卡牌数据至内存 ######");
        for (Chess c : chessList) {
            redisTemplate.opsForValue().set(CHESS_DB + c.getId(), c);
        }
        LOG.info("###### 成功将牌库数据写入 Redis 数据库中 ######");
    }

    /**
     * 获得指定id的卡牌副本对象
     *
     * @param chessId 指定卡牌id
     * @return 指定id的卡牌副本对象
     */
    public Chess getChess(int chessId) {
        Chess chess = new Chess((Chess) redisTemplate.opsForValue().get(CHESS_DB + chessId));
        return chess;
    }

    /**
     * 随机5张卡牌
     *
     * @return 返回包含随机5张卡牌的列表
     */
    public List<Chess> getRandomChess() {
        return getRandomChess(5);
    }

    /**
     * 随机 count 张卡牌
     *
     * @param count 指定的卡牌数量
     * @return 包含 count 张卡牌的列表
     */
    public List<Chess> getRandomChess(int count) {
        List<Chess> list = new ArrayList<>();
        Random random = new Random();
        LOG.info("###### 尝试从 Redis 数据库中随机读取 " + count + " 张卡牌 ######");
        for (int i = 0; i < count; ++i) {
            int chessId = random.nextInt(chessSize) + 1;
            Chess originChess = (Chess) redisTemplate.opsForValue().get(CHESS_DB + chessId);
//            LOG.info("###### 成功从 Redis 数据库中读取 " + chessId + " 号卡牌的原始数据 ######");
//            LOG.info("卡牌信息：" + originChess);
            if (originChess != null) {
                Chess chess = new Chess(originChess);
//                LOG.info("###### 生成新卡牌副本对象并存入列表中 ######");
                list.add(chess);
            } else {
                LOG.error("###### 随机卡牌过程发生错误！！！ ######");
                return null;
            }
        }
        LOG.info("###### 成功从 Redis 数据库中读取随机 " + count + " 张卡牌并生成副本列表返回 ######");
        return list;
    }

    public static String formatShowChessList(List<Chess> list) {
        StringBuilder builder = new StringBuilder();
        for (Chess c : list) {
            builder.append(String.format("卡牌ID： %d 卡牌名： %s", c.getId(), c.getName()));
        }
        return builder.toString();
    }


}

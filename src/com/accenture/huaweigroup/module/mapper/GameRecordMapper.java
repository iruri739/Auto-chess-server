package com.accenture.huaweigroup.module.mapper;

import com.accenture.huaweigroup.module.entity.GameRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRecordMapper {

    void insert(GameRecord gameRecord);

    void update(GameRecord gameRecord);

//    GameRecord findByPlayerId(int playerOneId, int playerTwoId);

    GameRecord getInitRecord(int playerOneId, int playerTwoId);

    GameRecord findById(int recordId);
}

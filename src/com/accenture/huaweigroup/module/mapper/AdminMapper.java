package com.accenture.huaweigroup.module.mapper;

import com.accenture.huaweigroup.module.entity.Chess;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;
import java.util.List;

@Repository
public interface AdminMapper {

    List<Chess> allChess();

    void insertChess_Land(Chess chess);

    void insertChess_Air(Chess chess);

    void insertChess_Sea(Chess chess);

    void deleteCard(int chess_id);


}

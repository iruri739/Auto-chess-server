package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.entity.ChessType;
import com.accenture.huaweigroup.module.mapper.AdminMapper;
import com.accenture.huaweigroup.module.mapper.ChessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService{

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ChessMapper chessMapper;

    public List<Chess> allChess() {
        return chessMapper.getAll();
    }

    public void insertChess(Chess chess)
    {
        if(chess.getType().equals(ChessType.LAND))
        {
            adminMapper.insertChess_Land(chess);
        }
        else if(chess.getType().equals(ChessType.AIR))
        {
            adminMapper.insertChess_Air(chess);
        }
        else
        {
            adminMapper.insertChess_Sea(chess);
        }
    }

    public void deleteChess(int chess_id)
    {
        adminMapper.deleteCard(chess_id);
    }

    public List<Chess> searchChess(String chess_name)
    {

        return chessMapper.searchChess(chess_name);
    }

    public void updatechCard(Chess chess)
    {
        adminMapper.updatechCard(chess);
    }



}

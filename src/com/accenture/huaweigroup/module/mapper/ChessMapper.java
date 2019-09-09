package com.accenture.huaweigroup.module.mapper;

import com.accenture.huaweigroup.module.entity.Chess;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface ChessMapper {

    List<Chess> getAll();

}
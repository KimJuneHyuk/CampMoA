package com.example.campmoa.services;

import com.example.campmoa.entities.bss.BoardEntity;
import com.example.campmoa.mappers.IBbsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.example.campmoa.services.BbsService")
public class BbsService {
    private final IBbsMapper bbsMapper;
    @Autowired
    public BbsService(IBbsMapper bbsMapper) {
        this.bbsMapper = bbsMapper;
    }

    public BoardEntity getBoard(String id) {
        BoardEntity boardEntity = this.bbsMapper.selectBoardById(id);
        return boardEntity;
    }
}

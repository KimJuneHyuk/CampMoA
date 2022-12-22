package com.example.campmoa.services;

import com.example.campmoa.entities.bss.ArticleEntity;
import com.example.campmoa.entities.bss.BoardEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.enums.bbs.WriteResult;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.mappers.IBbsMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.example.campmoa.services.BbsService")
public class BbsService {
    private final IBbsMapper bbsMapper;
    @Autowired
    public BbsService(IBbsMapper bbsMapper) {
        this.bbsMapper = bbsMapper;
    }



    public BoardEntity[] getBoard() {
//        BoardEntity[] 존재하는 값은 id , text 2개 이다.
//        레코드를 존재하는 래코드 한줄 을 하나의 배열 값으로 읽어 오기 위해서 배열을 썻다.
        return this.bbsMapper.selectBoardIdByText();
    }
//    public BoardEntity getBoard(String id) {
//        BoardEntity boardEntity = this.bbsMapper.selectBoardById(id);
//        return boardEntity;
//    }


    public Enum<? extends IResult> RegisterBoard (
            ArticleEntity article
    ) {
        BoardEntity board = this.bbsMapper.selectBoardById(article.getBoardValue());
        if (board == null) {
            return WriteResult.NO_SUCH_BOARD;
        }
        return this.bbsMapper.insertArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }





//    ==========================================================================
}

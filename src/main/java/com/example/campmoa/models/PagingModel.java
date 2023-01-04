package com.example.campmoa.models;

public class PagingModel {
    public final int countPerPage; //페이지당 표시할 게시글의 개수   10
    public final int totalCount; //전체 게시글 개수
    public final int requestPage; //요청한 페이지 번호          2
    public final int maxPage; //이동 가능한 최대 페이지
    public final int minPage; //이동 가능한 최소 페이지
    public final int startPage; //표시 시작 페이지         요청 페이지 기준으로 앞으로 4
    public final int endPage; //표시 끝 페이지            뒤로 4개
    public final int beforeAfterCount = 4;


    public PagingModel(int totalCount, int requestPage) {
        this(10,totalCount,requestPage);
    }
    public PagingModel(int countPerPage, int totalCount, int requestPage) {
        this.countPerPage = countPerPage;
        this.totalCount = totalCount;
        this.requestPage = requestPage;
        this.maxPage = (totalCount-1)/countPerPage +1;
//        50 -1(49) / 4.9....= 4      + =  5;
        this.minPage = 1;

        this.startPage = (requestPage - beforeAfterCount) < 1 ? minPage : (requestPage - beforeAfterCount) ;
//      2 - 1 =  4/10 = 0.4 * 10 = 4 + 1 = 5
        this.endPage = Math.min((requestPage + beforeAfterCount), maxPage);
//                                   1 + 3 = 4 , max =  3
    }



//    public final int countPerPage; //페이지당 표시할 게시글의 개수 perPageNum
//    public final int totalCount; //전체 게시글 개수
//    public final int requestPage; //요청한 페이지 번호  page:: 현재 페이지
//    public final int maxPage; //이동 가능한 최대 페이지
//    public final int minPage; //이동 가능한 최소 페이지
//    public final int startPage; //표시 시작 페이지
//    public final int endPage; //표시 끝 페이지
//
//
//    public PagingModel(int totalCount, int requestPage) {
//        this(10,totalCount,requestPage);
//    }
//    public PagingModel(int countPerPage, int totalCount, int requestPage) {
//        this.countPerPage = countPerPage;
//        this.totalCount = totalCount;
//        this.requestPage = requestPage;
//        this.maxPage = (totalCount-1)/countPerPage +1;
//        this.minPage = 1;
//        this.startPage = ((requestPage-1)/10) * 10 + 1;
//        this.endPage = Math.min(this.startPage + 9,this.maxPage);
//    }
}

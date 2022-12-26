package com.example.campmoa.entities.bbs;

import java.util.Objects;

public class BoardEntity {
    public  static final String ATTRIBUTE_NAME = "bbsBoard";
    public  static final String ATTRIBUTE_NAME_PLURAL = "bbsBoards";

    public static BoardEntity build() {
        return new BoardEntity();
    }
    private String id;
    private String text;

    public BoardEntity() {
    }

    public BoardEntity(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity board = (BoardEntity) o;
        return Objects.equals(id, board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

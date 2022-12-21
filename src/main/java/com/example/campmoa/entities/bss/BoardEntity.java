package com.example.campmoa.entities.bss;

import java.util.Objects;

public class BoardEntity {
    public  static final String ATTRIBUTE_NAME = "bbsBoard";
    public  static final String ATTRIBUTE_NAME_PLURAL = "bbsBoards";

    public static BoardEntity build() {
        return new BoardEntity();
    }
    private String value;
    private String text;

    public BoardEntity() {
    }

    public BoardEntity(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity board = (BoardEntity) o;
        return Objects.equals(value, board.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

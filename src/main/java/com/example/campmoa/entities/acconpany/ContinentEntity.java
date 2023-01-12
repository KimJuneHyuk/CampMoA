package com.example.campmoa.entities.acconpany;

import java.util.Objects;

public class ContinentEntity {
    public static final String ATTRIBUTE_NAME = "accompanyContinent";
    public static final String ATTRIBUTE_NAME_PLURAL = "accompanyContinents";

    public static ContinentEntity build() {
        return new ContinentEntity();
    }

    private String value;
    private String text;

    public ContinentEntity() {
    }

    public ContinentEntity(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContinentEntity that = (ContinentEntity) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String getValue() {
        return value;
    }

    public ContinentEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public ContinentEntity setText(String text) {
        this.text = text;
        return this;
    }
}

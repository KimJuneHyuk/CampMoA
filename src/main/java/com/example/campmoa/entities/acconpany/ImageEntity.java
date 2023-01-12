package com.example.campmoa.entities.acconpany;

import java.util.Date;
import java.util.Objects;

public class ImageEntity {
    public static final String ATTRIBUTE_NAME = "image";
    public static final String ATTRIBUTE_NAME_PLURAL = "images";

    public static ImageEntity build() {
        return new ImageEntity();
    }

    private int index;      // 번호
    private String userEmail; // 올린 사람
    private Date createdAt;  // 올린 시간
    private String name;   // 사진 이름
    private String mime;   // 파일 종류
    private byte[] data;   // 데이터 용량

    public ImageEntity() {
    }

    public ImageEntity(int index, String userEmail, Date createdAt, String name, String mime, byte[] data) {
        this.index = index;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.name = name;
        this.mime = mime;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public int getIndex() {
        return index;
    }

    public ImageEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ImageEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ImageEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getMime() {
        return mime;
    }

    public ImageEntity setMime(String mime) {
        this.mime = mime;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public ImageEntity setData(byte[] data) {
        this.data = data;
        return this;
    }
}

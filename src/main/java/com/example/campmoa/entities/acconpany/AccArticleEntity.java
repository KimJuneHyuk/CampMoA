package com.example.campmoa.entities.acconpany;

import java.util.Date;
import java.util.Objects;

public class AccArticleEntity {

    public  static final String ATTRIBUTE_NAME = "accArticle";
    public  static final String ATTRIBUTE_NAME_PLURAL = "accArticles";

    public static AccArticleEntity build() {
        return new AccArticleEntity();
    }

    private int index;
    private String userEmail;
    private Date createdAt;
    private String continentValue;
    private String countryValue;
    private String regionValue;
    private int capacity;
    private Date dateFrom;
    private Date dateTo;
    private byte[] coverImage;
    private String coverImageMime;
    private String title;
    private String content;

    public AccArticleEntity() {
    }

    public AccArticleEntity(int index, String userEmail, Date createdAt, String continentValue, String countryValue, String regionValue, int capacity, Date dateFrom, Date dateTo, byte[] coverImage, String coverImageMime, String title, String content) {
        this.index = index;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.continentValue = continentValue;
        this.countryValue = countryValue;
        this.regionValue = regionValue;
        this.capacity = capacity;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.coverImage = coverImage;
        this.coverImageMime = coverImageMime;
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccArticleEntity article = (AccArticleEntity) o;
        return index == article.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public int getIndex() {
        return index;
    }

    public AccArticleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public AccArticleEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public AccArticleEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getContinentValue() {
        return continentValue;
    }

    public AccArticleEntity setContinentValue(String continentValue) {
        this.continentValue = continentValue;
        return this;
    }

    public String getCountryValue() {
        return countryValue;
    }

    public AccArticleEntity setCountryValue(String countryValue) {
        this.countryValue = countryValue;
        return this;
    }

    public String getRegionValue() {
        return regionValue;
    }

    public AccArticleEntity setRegionValue(String regionValue) {
        this.regionValue = regionValue;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public AccArticleEntity setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public AccArticleEntity setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public AccArticleEntity setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public AccArticleEntity setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public String getCoverImageMime() {
        return coverImageMime;
    }

    public AccArticleEntity setCoverImageMime(String coverImageMime) {
        this.coverImageMime = coverImageMime;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AccArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public AccArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }
}

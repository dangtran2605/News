package com.tranvandang.news.Model;

public class News {
    String key, title ,detail, desctiption, url,imgUrl1,keyDitorial, keyCategory;
    Long countUser;
    Boolean status;

    public News() {
    }

    public News(String title, String detail, String description, String url, String imgUrl1, String keyDitorial, String keyCategory, Long countUser, Boolean status) {
        this.title = title;
        this.detail = detail;
        this.desctiption = description;
        this.url = url;
        this.imgUrl1 = imgUrl1;
        this.keyDitorial = keyDitorial;
        this.keyCategory = keyCategory;
        this.countUser = countUser;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String description) {
        this.desctiption = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getKeyDitorial() {
        return keyDitorial;
    }

    public void setKeyDitorial(String keyDitorial) {
        this.keyDitorial = keyDitorial;
    }

    public String getKeyCategory() {
        return keyCategory;
    }

    public void setKeyCategory(String keyCategory) {
        this.keyCategory = keyCategory;
    }

    public Long getCountUser() {
        return countUser;
    }

    public void setCountUser(Long countUser) {
        this.countUser = countUser;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

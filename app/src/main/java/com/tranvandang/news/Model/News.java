package com.tranvandang.news.Model;

public class News {
    String key, title ,detail, description, url,imgUrl1,keyDitorial, keyCategory;
    Long countUser;
    Boolean status;

    public News() {
    }

    public News(String title, String detail, String description, String url, String imgUrl1, Long countUser, Boolean status, String keyDitorial, String keyCategory) {
        this.title = title;
        this.detail = detail;
        this.description = description;
        this.url = url;
        this.imgUrl1 = imgUrl1;
        this.countUser = countUser;
        this.status = status;
        this.keyDitorial = keyDitorial;
        this.keyCategory = keyCategory;
    }

    public String getKeyCategory() {
        return keyCategory;
    }

    public void setKeyCategory(String keyCategory) {
        this.keyCategory = keyCategory;
    }

    public String getKeyDitorial() {
        return keyDitorial;
    }

    public void setKeyDitorial(String keyDitorial) {
        this.keyDitorial = keyDitorial;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

package com.tranvandang.news.ViewModel;

public class NewsDetail {
    String key, title ,detail, description, url,imgUrl1, logoDitoUrl,nameDitorial, nameCategory;
    Long countUser;
    Boolean status;

    public NewsDetail() {
    }

    public NewsDetail(String title, String detail, String description, String url, String imgUrl1, String logoDitoUrl, String nameDitorial, String nameCategory, Long countUser, Boolean status) {
        this.title = title;
        this.detail = detail;
        this.description = description;
        this.url = url;
        this.imgUrl1 = imgUrl1;
        this.logoDitoUrl = logoDitoUrl;
        this.nameDitorial = nameDitorial;
        this.nameCategory = nameCategory;
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

    public String getLogoDitoUrl() {
        return logoDitoUrl;
    }

    public void setLogoDitoUrl(String logoDitoUrl) {
        this.logoDitoUrl = logoDitoUrl;
    }

    public String getNameDitorial() {
        return nameDitorial;
    }

    public void setNameDitorial(String nameDitorial) {
        this.nameDitorial = nameDitorial;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
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

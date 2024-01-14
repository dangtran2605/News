package com.tranvandang.news.Model;

public class Bookmark {
    String key, keyUser, keyNews;

    public Bookmark() {

    }

    public Bookmark(String keyUser, String keyNews) {
        this.keyUser = keyUser;
        this.keyNews = keyNews;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getKeyNews() {
        return keyNews;
    }

    public void setKeyNews(String keyNews) {
        this.keyNews = keyNews;
    }
}

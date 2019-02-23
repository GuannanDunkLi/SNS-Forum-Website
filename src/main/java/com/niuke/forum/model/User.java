package com.niuke.forum.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "forum", type = "user")
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String head_url;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String headUrl) {
        this.head_url = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
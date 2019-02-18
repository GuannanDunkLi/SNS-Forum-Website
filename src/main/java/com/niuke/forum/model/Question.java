package com.niuke.forum.model;

import java.util.Date;

public class Question {
    private int id;
    private String title;
    private String content;
    private Date created_date;
    private int user_id;
    private int comment_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(Date createdDate) {
        this.created_date = createdDate;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public int getCommentCount() {
        return comment_count;
    }

    public void setCommentCount(int commentCount) {
        this.comment_count = commentCount;
    }
}
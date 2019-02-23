package com.niuke.forum.model;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "forum", type = "question")
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

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date createdDate) {
        this.created_date = createdDate;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int userId) {
        this.user_id = userId;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int commentCount) {
        this.comment_count = commentCount;
    }
}
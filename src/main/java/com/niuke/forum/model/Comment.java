package com.niuke.forum.model;

import java.util.Date;

public class Comment {
    private int id;
    private int user_id;
    private int entity_id;
    private int entity_type;
    private String content;
    private Date created_date;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public int getEntityId() {
        return entity_id;
    }

    public void setEntityId(int entityId) {
        this.entity_id = entityId;
    }

    public int getEntityType() {
        return entity_type;
    }

    public void setEntityType(int entityType) {
        this.entity_type = entityType;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

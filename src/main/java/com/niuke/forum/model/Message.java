package com.niuke.forum.model;

import java.util.Date;

public class Message {
    private int id;
    private int from_id;
    private int to_id;
    private String content;
    private Date created_date;
    private int has_read;
    private String conversation_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return from_id;
    }

    public void setFromId(int fromId) {
        this.from_id = fromId;
    }

    public int getToId() {
        return to_id;
    }

    public void setToId(int toId) {
        this.to_id = toId;
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

    public int getHasRead() {
        return has_read;
    }

    public void setHasRead(int hasRead) {
        this.has_read = hasRead;
    }

    public String getConversationId() {
        return conversation_id;
    }

    public void setConversationId(String conversationId) {
        this.conversation_id = conversationId;
    }
}
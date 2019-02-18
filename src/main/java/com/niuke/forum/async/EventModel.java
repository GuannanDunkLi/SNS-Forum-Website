package com.niuke.forum.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type; // 什么事件
    private int actorId; // 谁做的
    private int entityType; // 载体是什么
    private int entityId; // 载体id
    private int entityOwnerId; // 载体关联用户（方便今后功能，如站内信）
    private Map<String, String> exts = new HashMap<>(); // 扩展信息字段

    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public String getExts(String key) {
        return exts.get(key);
    }

    public EventModel setExts(String key, String value) {
        exts.put(key, value);
        return this;
    }

    // 发现这两个默认的getter，setter缺不了（否则handler里找不到），不知原因
    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
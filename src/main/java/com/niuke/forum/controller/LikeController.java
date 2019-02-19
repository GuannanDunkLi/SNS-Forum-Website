package com.niuke.forum.controller;

import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventProducer;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.Comment;
import com.niuke.forum.model.EntityType;
import com.niuke.forum.model.HostHolder;
import com.niuke.forum.service.CommentService;
import com.niuke.forum.service.LikeService;
import com.niuke.forum.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    @PostMapping(value = "/like")
    @ResponseBody
    public String like(int commentId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        // 获取点赞的那条评论
        Comment comment = commentService.getCommentById(commentId);
        // 异步队列发送私信给被赞人
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getEntityId())
                .setExts("questionId", String.valueOf(comment.getEntityId())));
        // 返回前端点赞数
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ForumUtil.getJsonString(0, String.valueOf(likeCount));
    }

    @PostMapping(value = "/dislike")
    @ResponseBody
    public String dislike(int commentId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ForumUtil.getJsonString(0, String.valueOf(likeCount));
    }
}
package com.niuke.forum.controller;

import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventProducer;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.*;
import com.niuke.forum.service.*;
import com.niuke.forum.utils.RedisKeyUtil;
import com.niuke.forum.utils.ForumUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

import java.util.*;

@Controller
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @PostMapping(value = "/followUser")
    @ResponseBody
    public String followUser(int userId) { // userId是关注者的id
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        // 发送异步事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        // 返回关注的人数
        return ForumUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @PostMapping(value = "/unfollowUser")
    @ResponseBody
    public String unfollowUser(int userId) { // userId是取消关注者的id
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        // 发送异步事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        // 返回关注的人数
        return ForumUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @PostMapping(value = "/followQuestion")
    @ResponseBody
    public String followQuestion(int questionId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        // 判断问题是否存在
        Question q = questionService.selectQuestionById(questionId);
        if (q == null) {
            return ForumUtil.getJsonString(1, "问题不存在");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        // 发送异步事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUser_id()));
        // 发送用户本人信息用于页面展示
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHead_url());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return ForumUtil.getJsonString(ret ? 0 : 1, info);
    }

    @PostMapping(value = "/unfollowQuestion")
    @ResponseBody
    public String unfollowQuestion(int questionId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        // 判断问题是否存在
        Question q = questionService.selectQuestionById(questionId);
        if (q == null) {
            return ForumUtil.getJsonString(1, "问题不存在");
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        // 发送异步事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUser_id()));
        // 发送用户本人信息用于页面展示
        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return ForumUtil.getJsonString(ret ? 0 : 1, info);
    }

    @GetMapping(value = "/user/{uid}/followers")
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    @GetMapping(value = "/user/{uid}/followees")
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
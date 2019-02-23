package com.niuke.forum.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.niuke.forum.async.EventHandler;
import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.*;
import com.niuke.forum.service.*;
import com.niuke.forum.utils.JedisAdapter;
import com.niuke.forum.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    FeedService feedService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    QuestionService questionService;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        // 往map里装触发用户信息
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHead_url());
        map.put("userName", actor.getName());
        // 当评论一个问题或关注一个问题（不考虑关注人）的时候
        if (model.getType() == EventType.COMMENT || (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.selectQuestionById(model.getEntityId());
            if (question == null) {
                return null;
            }
            // 往map里装问题信息
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        // 为了测试，把model的userId随机一下
//        Random r = new Random();
//        model.setActorId(1 + r.nextInt(10));
        // 构造一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            // 不支持的feed
            return;
        }
        feedService.addFeed(feed);
        // 获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        // 给未登录用户也推送
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
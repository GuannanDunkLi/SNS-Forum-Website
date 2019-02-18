package com.niuke.forum.controller;

import com.niuke.forum.model.HostHolder;
import com.niuke.forum.model.Message;
import com.niuke.forum.model.User;
import com.niuke.forum.model.ViewObject;
import com.niuke.forum.service.MessageService;
import com.niuke.forum.service.UserService;
import com.niuke.forum.utils.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GetMapping(value = "/msg/list")
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            int targetId = (message.getFromId() == localUserId ? message.getToId() : message.getFromId()); // 显示和我互动的用户
            vo.set("user", userService.getUser(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }

    @GetMapping(value = "/msg/detail")
    public String getConversationDetail(Model model, String conversationId) {
        try {
            // 获取消息列表
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            // 获取消息及相关用户
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @PostMapping(value = "/msg/addMessage")
    @ResponseBody
    public String addMessage(String toName, String content) {
        try {
            // 未登录要登陆
            if (hostHolder.getUser() == null) {
                return ForumUtil.getJsonString(999, "未登录");
            }
            // 检查用户是否存在
            User user = userService.selectUserByName(toName);
            if (user == null) {
                return ForumUtil.getJsonString(1, "用户不存在");
            }
            // 发送消息
            int fromId = hostHolder.getUser().getId();
            int toId = user.getId();
            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);
            return ForumUtil.getJsonString(0);
        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return ForumUtil.getJsonString(1, "发信失败");
        }
    }
}
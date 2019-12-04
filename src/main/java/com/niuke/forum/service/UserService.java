package com.niuke.forum.service;

import com.niuke.forum.dao.LoginTicketMapper;
import com.niuke.forum.dao.UserMapper;
import com.niuke.forum.model.User;
import com.niuke.forum.model.LoginTicket;
import com.niuke.forum.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    LoginTicketMapper loginTicketMapper;

    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();

        // 注册信息简单判断(Simple verification of register)
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "the username cannot be null");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "the password cannot be null");
            return map;
        }
        User user = selectUserByName(username);
        if (user != null) {
            map.put("msg", "the username has already registered");
            return map;
        }

        // 符合条件便注册用户(Register users if they meet the conditions)
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(ForumUtil.MD5(password + user.getSalt()));
        userMapper.insert(user);

        // 注册完成下发ticket之后自动登录(Sign in automatically after registering and distributing tickets)
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();

        // 登陆信息简单判断(Simple verification of login)
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "the username cannot be null");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "the password cannot be null");
            return map;
        }
        User user = selectUserByName(username);
        if (user == null) {
            map.put("msg", "the username has not registered");
            return map;
        }
        if (!ForumUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "the password is wrong");
            return map;
        }

        // 登陆成功下发ticket之后自动登录(Log in automatically after the ticket is issued successfully)
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public String addLoginTicket(int user_id) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUser_id(user_id);
        Date nowDate = new Date();
        nowDate.setTime(60 * 60 * 1000 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("_", ""));
        loginTicketMapper.insert(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket(ticket);
        loginTicket = loginTicketMapper.selectOne(loginTicket);
        loginTicket.setStatus(1);
        loginTicketMapper.updateByPrimaryKeySelective(loginTicket);
    }

    public User getUser(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public User selectUserByName(String username) {
        User user = new User();
        user.setName(username);
        return userMapper.selectOne(user);
    }
}
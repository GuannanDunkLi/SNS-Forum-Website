package com.niuke.forum.service;

import com.niuke.forum.dao.LoginTicketDAO;
import com.niuke.forum.dao.UserDAO;
import com.niuke.forum.model.User;
import com.niuke.forum.model.LoginTicket;
//import com.springboot.springboot.utils.ForumUtil;
import com.niuke.forum.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO lTicketsDAO;

    // 注册
    public Map<String,String > register(String username,String password){
        Map<String,String> map = new HashMap<>();
        // 后台的简单判断
        if(StringUtils.isEmpty(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return  map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg","用户名已被注册");
            return  map;
        }
        // 符合条件便注册用户
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ForumUtil.MD5(password + user.getSalt())); // 密码加salt再用MD5加密，安全性更高
        userDAO.addUser(user);
        // 注册完成下发ticket之后自动登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //登陆
    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<>();
        // 后台简单判断
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null){
            map.put("msg","用户名不存在");
            return map;
        }
        if (!ForumUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }
        // 登陆成功下发ticket之后自动登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public String addLoginTicket(int user_id){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(user_id);
        Date nowDate = new Date();
        nowDate.setTime(60*60*1000 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(0);
        // UUID生成的随机ticket有"_"，要替换掉
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("_",""));
        lTicketsDAO.addTicket(ticket);
        return ticket.getTicket(); // 返回ticket
    }

    public void logout(String ticket){
        lTicketsDAO.updateStatus(ticket,1);
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public  User selectUserByName(String name){
        return userDAO.selectByName(name);
    }
}
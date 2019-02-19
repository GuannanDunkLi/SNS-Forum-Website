package com.niuke.forum.controller;

import com.niuke.forum.service.UserService;
//import com.springboot.springboot.async.EventModel;
//import com.springboot.springboot.async.EventProducer;
//import com.springboot.springboot.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// 首页的登录、注册功能
@Controller
public class LoginRegisterController {
    private static final Logger logger = LoggerFactory.getLogger(LoginRegisterController.class);

    @Autowired
    UserService userService;

//    @Autowired
//    EventProducer eventProducer;

    //注册
    @PostMapping(value = "/reg")
    public String reg(Model model, String username, String password,
                      @RequestParam(value = "next", required = false) String next,
                      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, String> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/"); // 在同一应用服务器内共享cookie
                response.addCookie(cookie); // ticket下发到客户端（浏览器）存储
                // 当读取到的next字段不为空的话跳转
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String register(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next); // 把next参数放在view里
        return "login";
    }

    //登陆
    @PostMapping(value = "/login")
    public String login(Model model, String username, String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/"); // 在同一应用服务器内共享cookie
                response.addCookie(cookie);
//                //用户登陆完设置一个事件
//                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
//                        .setExts("username", username).setExts("email", "1390505180@qq.com"));
//                // .setActorId((int)map.get("user_id")));          //用户登陆完之后判断是谁登录的
                // 当读取到的next字段不为空的话跳转
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }
    }

    @GetMapping(value = "/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/"; // 这一步会再次执行ticket拦截器，所以首页没有个人登陆显示了
    }
}
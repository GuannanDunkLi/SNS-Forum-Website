package com.niuke.forum.controller;

import com.niuke.forum.service.UserService;
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

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping(value = "/reg")
    public String reg(Model model, HttpServletResponse response,
                      @RequestParam String username,
                      @RequestParam String password,
                      @RequestParam(required = false) String next) {
        try {
            Map<String, String> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);

                // 当读取到的next字段不为空的话跳转(Redirecting when the "next" field is not empty)
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }

                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("Registration error: " + e.getMessage());
            return "login";
        }
    }

    @PostMapping(value = "/login")
    public String login(Model model, HttpServletResponse response,
                        @RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false) String next,
                        @RequestParam(defaultValue = "false") boolean rememberme) {
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    // 不设置默认为-1(default value is -1)，即关闭浏览器就消失(Closing the browser and disappearing)
                    // 以秒为单位(in seconds)
                    cookie.setMaxAge(3600*24);
                }
                response.addCookie(cookie);

                // 当读取到的next字段不为空的话跳转(Redirecting when the "next" field is not empty)
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }

                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("Login error: " + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String register(Model model, @RequestParam(required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @GetMapping(value = "/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
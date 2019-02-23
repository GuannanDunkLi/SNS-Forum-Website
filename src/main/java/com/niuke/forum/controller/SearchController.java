package com.niuke.forum.controller;

import com.niuke.forum.model.EntityType;
import com.niuke.forum.model.Question;
import com.niuke.forum.service.FollowService;
import com.niuke.forum.service.QuestionService;
import com.niuke.forum.service.SearchService;
import com.niuke.forum.service.UserService;
import com.niuke.forum.model.ViewObject;
import com.niuke.forum.utils.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    // 保存数据库所有question到es
    @GetMapping(value = "/save")
    @ResponseBody
    public String save() {
        List<Question> questions = questionService.selectLatestQuestions(0, 0, 500); // 500是按数据量写的
        searchService.save(questions);
        return ForumUtil.getJsonString(0);
    }

    @GetMapping(value = "/search")
    public String search(Model model, @RequestParam("q") String keyword, @RequestParam(value = "p", defaultValue = "1") int p) {
        try {
            int offset = (p - 1) * 10;
            List<Question> questionList = searchService.testSearch(keyword, offset, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                ViewObject vo = new ViewObject();
                vo.set("question", question);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUser(question.getUser_id()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            logger.error("搜索失败" + e.getMessage());
        }
        return "result";
    }
}
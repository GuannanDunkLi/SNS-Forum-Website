package com.niuke.forum.service;

import com.niuke.forum.dao.QuestionDAO;
import com.niuke.forum.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;

    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    // 添加问题
    public int addQuestion(Question question) {
        // HTML代码过滤，就是把html语言进行转义
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0; // 这里question.getId()就是存进数据库后对应的Id了
    }

    public Question selectQuestionById(int id) {
        return questionDAO.selectQuestionById(id);
    }

    //更新评论的数量
    public int updateCommentCount(int id, int comment_count) {
        return questionDAO.updateCommentCount(id, comment_count);
    }
}
package com.niuke.forum.service;

import com.niuke.forum.model.Question;
import com.niuke.forum.repository.QuestionRepository;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private QuestionRepository questionRepository;

    public void save(List<Question> questions) {
        questionRepository.saveAll(questions);
    }

    public List<Question> testSearch(String keywords, int offset, int limit) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(keywords);
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "created_date"));
        Page<Question> page = questionRepository.search(builder, pageable);
        return page.getContent();
    }
}
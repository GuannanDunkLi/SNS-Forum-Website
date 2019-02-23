package com.niuke.forum.repository;

import com.niuke.forum.model.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface QuestionRepository extends ElasticsearchRepository<Question,Long>{
}
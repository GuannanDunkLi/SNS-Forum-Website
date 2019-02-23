package com.niuke.forum.repository;

import com.niuke.forum.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends ElasticsearchRepository<User,Long>{
}
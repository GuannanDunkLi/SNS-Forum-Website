package com.niuke.forum.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user")
@Document(indexName = "forum", type = "user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String head_url;
}
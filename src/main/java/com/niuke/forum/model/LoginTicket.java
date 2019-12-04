package com.niuke.forum.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "login_ticket")
public class LoginTicket {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer user_id;
    private Date expired;
    private Integer status; // 0有效，1无效(0 is valid, 1 is invalid)
    private String ticket;
}
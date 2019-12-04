package com.niuke.forum.dao;

import com.niuke.forum.model.LoginTicket;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface LoginTicketMapper extends Mapper<LoginTicket> {
}

package com.niuke.forum.dao;

import com.niuke.forum.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String TABLE_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + TABLE_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", TABLE_FIELDS, ") values(#{name}, #{password}, #{salt}, #{head_url})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where name=#{name}"})
    User selectByName(String name);

    @Update({"update", TABLE_NAME, "set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    @Delete({"delete from", TABLE_NAME, "where id = #{id}"})
    void deleteUserById(int id);
}
package org.tsubaki.DataBase.Mybatis.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.tsubaki.DataBase.Mybatis.Entity.User;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from User where userID = #{id}")
    public User selectByUserID(String id);

    @Update("Update User set " +
            "userName=#{user.userName}, " +
            "userHead=#{user.userHead}," +
            "userPassword=#{user.userPassword}," +
            "userPassword=#{user.userPassword}," +
            "where userID=#{user.userID}")
    public void updateUser(User user);

    @Update("Update User set userName= #{newUserName} where userID= #{id}")
    public void updateUserName(String id,String newUserName);

    @Update("Update User set userPassword= #{newUserPassword} where userID=#{id}")
    public void updateUsePassword(String id,String newUserPassword);

    @Update("Update User set userHead= #{newUserHead} where userID=#{id}")
    public void updateUseHead(String id,String newUserHead);

    @Update("Update User set userIntroduction= #{newUserIntroduction} where userID=#{id}")
    public void updateUserIntroduction(String id,String newUserIntroduction);

    @Insert("Insert into User (userID,userName,userPassword,userHead,userIntroduction) " +
            "VALUES (#{user.userID},#{user.userName},#{user.userPassword},#{user.userHead},#{user.userIntroduction})")
    public void  insertUser(User user);
}

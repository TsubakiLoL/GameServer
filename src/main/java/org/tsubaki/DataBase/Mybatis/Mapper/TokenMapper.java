package org.tsubaki.DataBase.Mybatis.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.tsubaki.DataBase.Mybatis.Entity.Token;


@Mapper
public interface TokenMapper extends BaseMapper<Token> {
    @Select("select * from Token where tokenID = #{id}")
    public Token selectByTokenID(String id);

    @Insert("Insert into Token (tokenID,tokenUser,tokenRel,tokenTime) " +
            "VALUES (#{token.tokenID},#{token.tokenUser},#{token.tokenRel},#{token.tokenTime})")
    public void insertToken(@Param("token") Token token);

}

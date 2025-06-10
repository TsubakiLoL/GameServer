package org.tsubaki.DataBase.Mybatis.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tsubaki.DataBase.Mybatis.Entity.Token;
import org.tsubaki.DataBase.Mybatis.Entity.User;
import org.tsubaki.DataBase.Mybatis.Mapper.TokenMapper;
import org.tsubaki.DataBase.Mybatis.Mapper.UserMapper;
import org.tsubaki.Tool.TimeTool;
import org.tsubaki.Tool.UUIDTool;

import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    TokenMapper tokenMapper;

    @Autowired
    UserMapper userMapper;

    public boolean hasToken(String tokenID){
        return tokenMapper.selectByTokenID(tokenID)!=null;
    }
    public String addNowToken(String tokenUser, LocalDateTime validTime){
        if(userMapper.selectByUserID(tokenUser)==null){
            return null;
        }
        try {
            String uuidString = UUIDTool.generateUUIDWithoutHyphen();
            while(hasToken(uuidString)){
                uuidString=UUIDTool.generateUUIDWithoutHyphen();
            }
            Token newToken =new Token();
            newToken.setTokenID(uuidString);
            newToken.setTokenUser(tokenUser);
            newToken.setTokenRel(TimeTool.getNowTimeString());
            newToken.setTokenTime(TimeTool.toDbString(validTime));
            tokenMapper.insertToken(newToken);
            return uuidString;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public User getTokenUser(String tokenID){
        if(!hasToken(tokenID)){
            return null;
        }
        try{
            Token token=tokenMapper.selectByTokenID(tokenID);
            if(!token.isTokenValid()){
                return null;
            }
            String userID=token.getTokenUser();
            return userMapper.selectByUserID(userID);
        }catch (Exception e){
            return null;
        }
    }
    public boolean isTokenValid(String tokenID){
        if(!hasToken(tokenID)){
            return false;
        }
        try {
            Token token = tokenMapper.selectByTokenID(tokenID);
            return token.isTokenValid();
        }catch (Exception e){
            return false;
        }
    }
}

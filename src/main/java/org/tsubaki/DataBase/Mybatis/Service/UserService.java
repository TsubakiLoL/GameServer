package org.tsubaki.DataBase.Mybatis.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tsubaki.DataBase.Mybatis.Entity.User;
import org.tsubaki.DataBase.Mybatis.Mapper.UserMapper;



@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    //通过ID搜索用户
    public  User selectUserByID(String id){
        return  userMapper.selectByUserID(id);
    }

    public boolean hasUser(String id){
        return selectUserByID(id)!=null;
    }

    //添加用户
    public boolean addUser(User user){
        if(selectUserByID(user.getUserID())!=null){
            return false;
        }
        try {
            userMapper.insertUser(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean updateUser(User user){
        if(!hasUser(user.getUserID())){
            return false;
        }
        try {
            userMapper.updateUser(user);
            return true;
        }catch (Exception e){
            return  false;
        }
    }


    public boolean updateUserName(String id,String newUserName){
        if(!hasUser(id)){
            return false;
        }
        try {
            userMapper.updateUserName(id,newUserName);
            return true;
        }catch (Exception e){
            return  false;
        }
    }


    public boolean updateUsePassword(String id,String newUserPassword){
        if(!hasUser(id)){
            return false;
        }
        try {
            userMapper.updateUsePassword(id,newUserPassword);
            return true;
        }catch (Exception e){
            return  false;
        }
    }


    public boolean updateUseHead(String id,String newUserHead){
        if(!hasUser(id)){
            return false;
        }
        try {
            userMapper.updateUseHead(id,newUserHead);
            return true;
        }catch (Exception e){
            return  false;
        }
    }


    public boolean updateUserIntroduction(String id,String newUserIntroduction){
        if(!hasUser(id)){
            return false;
        }
        try {
            userMapper.updateUserIntroduction(id,newUserIntroduction);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
}

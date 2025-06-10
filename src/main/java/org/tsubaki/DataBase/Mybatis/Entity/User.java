package org.tsubaki.DataBase.Mybatis.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.tsubaki.Tool.MD5Tool;

@TableName("User")
public class User {

    @TableId
    private String userID;

    @TableField("userName")
    private String userName;

    @TableField("userPassword")
    private String userPassword;

    @TableField("userHead")
    private String userHead;

    @TableField("userIntroduction")
    private String userIntroduction;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public boolean isPasswordPass(String password){
        return password.equals(MD5Tool.generateMD5(userPassword));
    }
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userHead='" + userHead + '\'' +
                ", userIntroduction='" + userIntroduction + '\'' +
                '}';
    }
}

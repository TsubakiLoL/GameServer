package org.tsubaki.DataBase.Mybatis.Entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.tsubaki.Tool.TimeTool;

import java.time.LocalDateTime;

@TableName("Token")
public class Token {

    @TableId
    private String tokenID;


    @TableField("tokenUser")
    private String tokenUser;

    @TableField("tokenRel")
    private String tokenRel;

    @TableField("tokenTime")
    private String tokenTime;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String getTokenRel() {
        return tokenRel;
    }

    public void setTokenRel(String tokenRel) {
        this.tokenRel = tokenRel;
    }

    public String getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(String tokenTime) {
        this.tokenTime = tokenTime;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenID='" + tokenID + '\'' +
                ", tokenUser='" + tokenUser + '\'' +
                ", tokenRel='" + tokenRel + '\'' +
                ", tokenTime='" + tokenTime + '\'' +
                '}';
    }


    //Token是否有效
    public boolean isTokenValid() {
        LocalDateTime tokenR = TimeTool.fromDbString(tokenRel); // Token发布时间
        LocalDateTime tokenT = TimeTool.fromDbString(tokenTime); // Token自发布后有效时间
        LocalDateTime now = LocalDateTime.now(); // 当前时间

        if(tokenR==null||tokenT==null){
            return false;
        }
        // 计算Token的过期时间 = 发布时间 + 有效时长
        LocalDateTime expireTime = tokenR.plusSeconds(tokenT.getSecond())
                .plusMinutes(tokenT.getMinute())
                .plusHours(tokenT.getHour())
                .plusDays(tokenT.getDayOfMonth() )
                .plusMonths(tokenT.getMonthValue())
                .plusYears(tokenT.getYear() );

        // 判断当前时间是否在有效期内
        return now.isBefore(expireTime);
    }
}

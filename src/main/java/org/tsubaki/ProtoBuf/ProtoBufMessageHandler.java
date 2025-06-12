package org.tsubaki.ProtoBuf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.DataBase.Mybatis.Entity.User;
import org.tsubaki.DataBase.Mybatis.Service.TokenService;
import org.tsubaki.DataBase.Mybatis.Service.UserService;
import org.tsubaki.ProtoBuf.Event.Request.ProtoBufEventLoginRequestNormal;
import org.tsubaki.ProtoBuf.Event.Request.ProtoBufEventLoginRequestToken;
import org.tsubaki.ProtoBuf.Event.Response.ProtoBufEventLoginQuitResponse;
import org.tsubaki.ProtoBuf.Event.Response.ProtoBufEventLoginResponseFailed;
import org.tsubaki.ProtoBuf.Event.Response.ProtoBufEventLoginResponseSucceed;
import org.tsubaki.MessageQueue.MessageCallback;
import org.tsubaki.MessageQueue.MessageEvent;
import org.tsubaki.MessageQueue.MessageQueue;
import org.tsubaki.ProtoBuf.Event.User.UserEventOffline;
import org.tsubaki.ProtoBuf.Event.User.UserEventOnline;
import org.tsubaki.Tool.BidirectionalMap;
import org.tsubaki.Web.Event.WebSocketEventSessionClose;
import org.tsubaki.Web.Event.WebSocketEventSessionMessage;

import java.time.LocalDateTime;


@Component
public class ProtoBufMessageHandler {
    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    BidirectionalMap<String,String> sessionIDToUserMap=new BidirectionalMap<String,String>();
    public ProtoBufMessageHandler(){
        MessageCallback onMessageCallback = new MessageCallback() {
            @Override
            public void event_get(MessageEvent event) {

                if (event instanceof WebSocketEventSessionMessage eventReal) {
                    processBinaryMessage(((WebSocketEventSessionMessage) event).sessionID,((WebSocketEventSessionMessage) event).message);

                }
            }
        };
        MessageQueue.m.take_in_event(WebSocketEventSessionMessage.class, onMessageCallback);
        MessageCallback onCloseCallback = new MessageCallback() {
            @Override
            public void event_get(MessageEvent event) {

                if (event instanceof WebSocketEventSessionClose eventReal) {
                    String sessionID=eventReal.sessionID;
                    if(sessionIDToUserMap.containsKey(sessionID)){
                        MessageQueue.m.push_event(new UserEventOffline(sessionIDToUserMap.getValue(sessionID)));
                        sessionIDToUserMap.removeByKey(sessionID);
                    }

                }
            }
        };
        MessageQueue.m.take_in_event(WebSocketEventSessionClose.class, onCloseCallback);
    }


    //处理二进制消息
    public void processBinaryMessage(String sessionID, BinaryMessage message){

        // 获取 DataBuffer

        try {
            MessageOuterClass.Message gameMessage= MessageOuterClass.Message.parseFrom(message.getPayload());
            switch (gameMessage.getType()) {
                case LOGIN_REQUEST_NORMAL -> {
                    MessageOuterClass.LoginRequestNormal loginRequestNormal = gameMessage.getLoginRequestNormal();
                    MessageQueue.m.push_event(new ProtoBufEventLoginRequestNormal(sessionID, loginRequestNormal.getId(), loginRequestNormal.getPassword()));
                    processLoginNormal(sessionID, loginRequestNormal);
                }
                case LOGIN_REQUEST_TOKEN -> {
                    MessageOuterClass.LoginRequestToken loginRequestToken = gameMessage.getLoginRequestToken();
                    MessageQueue.m.push_event(new ProtoBufEventLoginRequestToken(sessionID, loginRequestToken.getToken()));
                    processLoginToken(sessionID, loginRequestToken);
                }
                case CREATE_ROOM_REQUEST -> {


                }
                case ENTER_ROOM_REQUEST -> {

                }
                case KICK_ROOM_USER_REQUEST -> {

                }
                case QUIT_ROOM_REQUEST -> {

                }
                case CHANGE_ROOM_OWNER_REQUEST -> {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void processLoginNormal(String sessionID, MessageOuterClass.LoginRequestNormal loginRequestNormal){
        if(loginRequestNormal==null){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, MessageOuterClass.LoginFailedCode.INVALID_ID));
            return;
        }
        String id=loginRequestNormal.getId();
        String password=loginRequestNormal.getPassword();
        if(!userService.hasUser(id)){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, MessageOuterClass.LoginFailedCode.INVALID_ID));
            return;
        }
        User user =userService.selectUserByID(id);
        if(!user.isPasswordPass(password)){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, MessageOuterClass.LoginFailedCode.INVALID_PASSWORD));
        }

        MessageOuterClass.User userOut=buildUser(user.getUserID(), user.getUserName(),user.getUserHead(),user.getUserIntroduction());

        String tokenID=tokenService.addNowToken(user.getUserID(), LocalDateTime.of(0,1,1,0,0));
        if(tokenID==null){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, MessageOuterClass.LoginFailedCode.INVALID_ID));
            return;
        }
        MessageQueue.m.push_event(ProtoBufEventLoginResponseSucceed.build(sessionID,userOut,tokenID));
        if(sessionIDToUserMap.containsValue(user.getUserID())){
            String beforeSessionID=sessionIDToUserMap.getKey(user.getUserID());
            MessageQueue.m.push_event(ProtoBufEventLoginQuitResponse.build(beforeSessionID,"本账号异地登录"));
            MessageQueue.m.push_event(new UserEventOffline(user.getUserID()));
        }

        sessionIDToUserMap.put(sessionID,user.getUserID());
        MessageQueue.m.push_event(new UserEventOnline(user.getUserID()));

        return;

    }
    public void processLoginToken(String sessionID, MessageOuterClass.LoginRequestToken loginRequestToken){
        if(loginRequestToken==null){
            System.out.println("无效消息");
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, MessageOuterClass.LoginFailedCode.INVALID_TOKEN));
            return;
        }
        String tokenID=loginRequestToken.getToken();
        if(!tokenService.isTokenValid(tokenID)){
            System.out.println("token无效");
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, MessageOuterClass.LoginFailedCode.INVALID_TOKEN));
            return;
        }
        User user=tokenService.getTokenUser(tokenID);
        if(sessionIDToUserMap.containsValue(user.getUserID())){
            String beforeSessionID=sessionIDToUserMap.getKey(user.getUserID());
            MessageQueue.m.push_event(ProtoBufEventLoginQuitResponse.build(beforeSessionID,"本账号异地登录"));
            MessageQueue.m.push_event(new UserEventOffline(user.getUserID()));
        }
        MessageOuterClass.User userOut=buildUser(user.getUserID(), user.getUserName(),user.getUserHead(),user.getUserIntroduction());
        MessageQueue.m.push_event(ProtoBufEventLoginResponseSucceed.build(sessionID,userOut,tokenID));
        sessionIDToUserMap.put(sessionID,user.getUserID());
        MessageQueue.m.push_event(new UserEventOnline(user.getUserID()));
    }


    MessageOuterClass.User buildUser(String userID, String userName, String userHead, String userIntroduction){
        MessageOuterClass.User.Builder userBuilder= MessageOuterClass.User.newBuilder();
        userBuilder.setName(userName)
                .setHead(userHead)
                .setId(userID)
                .setIntroduction(userIntroduction);
        MessageOuterClass.User userOut=userBuilder.build();
        return userBuilder.build();
    }
}

package org.tsubaki.ProtoBuf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.DataBase.Mybatis.Entity.User;
import org.tsubaki.DataBase.Mybatis.Service.TokenService;
import org.tsubaki.DataBase.Mybatis.Service.UserService;
import org.tsubaki.ProtoBuf.Event.Request.ProtoBufEventLoginRequestNormal;
import org.tsubaki.ProtoBuf.Event.Request.ProtoBufEventLoginRequestToken;
import org.tsubaki.ProtoBuf.Event.Response.ProtoBufEventLoginResponseFailed;
import org.tsubaki.ProtoBuf.Event.Response.ProtoBufEventLoginResponseSucceed;
import org.tsubaki.MessageQueue.MessageCallback;
import org.tsubaki.MessageQueue.MessageEvent;
import org.tsubaki.MessageQueue.MessageQueue;
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


                }
            }
        };
        MessageQueue.m.take_in_event(WebSocketEventSessionClose.class, onCloseCallback);
    }


    //处理二进制消息
    public void processBinaryMessage(String sessionID, BinaryMessage message){

        // 获取 DataBuffer

        try {
            Message.GameMessage gameMessage=Message.GameMessage.parseFrom(message.getPayload());
            switch (gameMessage.getType()) {
                case LOGIN_REQUEST_NORMAL -> {
                    Message.LoginRequestNormal loginRequestNormal = gameMessage.getLoginRequestNormal();
                    MessageQueue.m.push_event(new ProtoBufEventLoginRequestNormal(sessionID, loginRequestNormal.getId(), loginRequestNormal.getPassword()));
                    processLoginNormal(sessionID, loginRequestNormal);
                }
                case LOGIN_REQUEST_TOKEN -> {
                    Message.LoginRequestToken loginRequestToken = gameMessage.getLoginRequestToken();
                    MessageQueue.m.push_event(new ProtoBufEventLoginRequestToken(sessionID, loginRequestToken.getToken()));
                    procesLoginToken(sessionID, loginRequestToken);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }



    public void processLoginNormal(String sessionID, Message.LoginRequestNormal loginRequestNormal){
        if(loginRequestNormal==null){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, Message.LoginFailedCode.INVALID_ID));
            return;
        }
        String id=loginRequestNormal.getId();
        String password=loginRequestNormal.getPassword();
        if(!userService.hasUser(id)){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, Message.LoginFailedCode.INVALID_ID));
            return;
        }
        User user =userService.selectUserByID(id);
        if(!user.isPasswordPass(password)){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, Message.LoginFailedCode.INVALID_PASSWORD));
        }
        Message.User.Builder userBuilder= Message.User.newBuilder();
        userBuilder.setName(user.getUserName())
                    .setHead(user.getUserHead())
                    .setId(user.getUserID())
                    .setIntroduction(user.getUserIntroduction());
        Message.User userOut=userBuilder.build();
        String tokenID=tokenService.addNowToken(user.getUserID(), LocalDateTime.of(0,1,1,0,0));
        if(tokenID==null){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, Message.LoginFailedCode.INVALID_ID));
            return;
        }
        MessageQueue.m.push_event(ProtoBufEventLoginResponseSucceed.build(sessionID,userOut,tokenID));
        if(sessionIDToUserMap.containsValue(user.getUserID())){
            String beforeSessionID=sessionIDToUserMap.getKey(user.getUserID());

        }
        sessionIDToUserMap.put(sessionID,user.getUserID());
        return;

    }
    public void procesLoginToken(String sessionID,Message.LoginRequestToken loginRequestToken){
        if(loginRequestToken==null){
            MessageQueue.m.push_event(ProtoBufEventLoginResponseFailed.build(sessionID, Message.LoginFailedCode.INVALID_ID));
            return;
        }


    }

}

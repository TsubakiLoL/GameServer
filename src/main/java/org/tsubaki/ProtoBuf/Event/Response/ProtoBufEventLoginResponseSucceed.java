package org.tsubaki.ProtoBuf.Event.Response;

import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.ProtoBuf.MessageOuterClass;
import org.tsubaki.Web.Event.WebSocketEventSessionMessageSendRequest;

public class ProtoBufEventLoginResponseSucceed extends WebSocketEventSessionMessageSendRequest {
    MessageOuterClass.User user;

    String token;


    public ProtoBufEventLoginResponseSucceed(String sessionID, BinaryMessage message, MessageOuterClass.User user, String token) {
        super(sessionID, message);
        this.user = user;
        this.token = token;
    }

    public static ProtoBufEventLoginResponseSucceed build(String sessionID, MessageOuterClass.User user, String token){
        MessageOuterClass.Message.Builder newMessageBuilder= MessageOuterClass.Message.newBuilder();
        newMessageBuilder.setType(MessageOuterClass.MessageType.LOGIN_RESPONSE_SUCCEED);
        MessageOuterClass.LoginResponseSucceed.Builder responseBuilder= MessageOuterClass.LoginResponseSucceed.newBuilder();
        responseBuilder.setToken(token);
        responseBuilder.setUser(user);
        newMessageBuilder.setLoginResponseSucceed(responseBuilder);
        BinaryMessage messageOut=new BinaryMessage(newMessageBuilder.build().toByteArray());
        return new ProtoBufEventLoginResponseSucceed(sessionID,messageOut,user,token);
    }

    @Override
    public String toString() {
        return "ProtoBufEventLoginResponseSucceed{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", message=" + message +
                ", sessionID='" + sessionID + '\'' +
                '}';
    }
}

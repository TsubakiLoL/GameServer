package org.tsubaki.ProtoBuf.Event.Response;

import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.ProtoBuf.Message;
import org.tsubaki.Web.Event.WebSocketEventSessionMessageSendRequest;

public class ProtoBufEventLoginResponseSucceed extends WebSocketEventSessionMessageSendRequest {
    Message.User user;

    String token;


    public ProtoBufEventLoginResponseSucceed(String sessionID, BinaryMessage message, Message.User user, String token) {
        super(sessionID, message);
        this.user = user;
        this.token = token;
    }

    public static ProtoBufEventLoginResponseSucceed build(String sessionID, Message.User user, String token){
        Message.GameMessage.Builder newMessageBuilder= Message.GameMessage.newBuilder();
        newMessageBuilder.setType(Message.MessageType.LOGIN_RESPONSE_SUCCEED);
        Message.LoginResponseSucceed.Builder responseBuilder=Message.LoginResponseSucceed.newBuilder();
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

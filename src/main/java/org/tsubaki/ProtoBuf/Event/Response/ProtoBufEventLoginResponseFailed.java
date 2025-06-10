package org.tsubaki.ProtoBuf.Event.Response;

import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.ProtoBuf.Message;
import org.tsubaki.Web.Event.WebSocketEventSessionMessageSendRequest;

public class ProtoBufEventLoginResponseFailed extends WebSocketEventSessionMessageSendRequest {
    Message.LoginFailedCode code;
    public ProtoBufEventLoginResponseFailed(String sessionID, BinaryMessage message,Message.LoginFailedCode code) {
        super(sessionID, message);
        this.code=code;
    }

    public static ProtoBufEventLoginResponseFailed build(String sessionID, Message.LoginFailedCode code){

        Message.GameMessage.Builder newMessageBuilder= Message.GameMessage.newBuilder();
        newMessageBuilder.setType(Message.MessageType.LOGIN_RESPONSE_FAILED);
        Message.LoginResponseFailed.Builder responseBuilder=Message.LoginResponseFailed.newBuilder();
        responseBuilder.setCode(code);
        newMessageBuilder.setLoginResponseFailed(responseBuilder);
        BinaryMessage messageOut=new BinaryMessage(newMessageBuilder.build().toByteArray());
        return new ProtoBufEventLoginResponseFailed(sessionID,messageOut,code);
    }

    @Override
    public String toString() {
        return "ProtoBufEventLoginResponseFailed{" +
                "code=" + code +
                ", message=" + message +
                ", sessionID='" + sessionID + '\'' +
                '}';
    }
}

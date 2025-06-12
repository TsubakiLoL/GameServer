package org.tsubaki.ProtoBuf.Event.Response;

import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.ProtoBuf.MessageOuterClass;
import org.tsubaki.Web.Event.WebSocketEventSessionMessageSendRequest;

public class ProtoBufEventLoginResponseFailed extends WebSocketEventSessionMessageSendRequest {
    MessageOuterClass.LoginFailedCode code;
    public ProtoBufEventLoginResponseFailed(String sessionID, BinaryMessage message, MessageOuterClass.LoginFailedCode code) {
        super(sessionID, message);
        this.code=code;
    }

    public static ProtoBufEventLoginResponseFailed build(String sessionID, MessageOuterClass.LoginFailedCode code){

        MessageOuterClass.Message.Builder newMessageBuilder= MessageOuterClass.Message.newBuilder();
        newMessageBuilder.setType(MessageOuterClass.MessageType.LOGIN_RESPONSE_FAILED);
        MessageOuterClass.LoginResponseFailed.Builder responseBuilder= MessageOuterClass.LoginResponseFailed.newBuilder();
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

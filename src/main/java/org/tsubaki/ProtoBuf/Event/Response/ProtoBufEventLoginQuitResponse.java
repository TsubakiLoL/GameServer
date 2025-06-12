package org.tsubaki.ProtoBuf.Event.Response;

import org.springframework.web.socket.BinaryMessage;
import org.tsubaki.ProtoBuf.MessageOuterClass;
import org.tsubaki.Web.Event.WebSocketEventSessionMessageSendRequest;

public class ProtoBufEventLoginQuitResponse extends WebSocketEventSessionMessageSendRequest {
    String reason;
    public ProtoBufEventLoginQuitResponse(String sessionID, BinaryMessage message,String reason) {
        super(sessionID, message);
        this.reason=reason;
    }

    public static ProtoBufEventLoginQuitResponse build(String sessionID,String reason){
        MessageOuterClass.Message.Builder newMessageBuilder= MessageOuterClass.Message.newBuilder();
        newMessageBuilder.setType(MessageOuterClass.MessageType.LOGIN_QUIT_RESPONSE);
        MessageOuterClass.LoginQuitResponse.Builder builder= MessageOuterClass.LoginQuitResponse.newBuilder();
        builder.setReason(reason);
        newMessageBuilder.setLoginQuitResponse(builder);
        BinaryMessage messageOut=new BinaryMessage(newMessageBuilder.build().toByteArray());

        return new ProtoBufEventLoginQuitResponse(sessionID,messageOut,reason);
    }
}

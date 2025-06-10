package org.tsubaki.Web.Event;

import org.springframework.web.socket.BinaryMessage;

public class WebSocketEventSessionMessageSendRequest extends WebSocketEventSession {
    public BinaryMessage message;
    public WebSocketEventSessionMessageSendRequest(String sessionID, BinaryMessage message) {
        super(sessionID);
        this.message=message;
    }

}

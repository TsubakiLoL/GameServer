package org.tsubaki.Web.Event;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.tsubaki.MessageQueue.MessageEvent;

public class WebSocketEventSessionMessage extends WebSocketEventSession {
    public BinaryMessage message;


    public WebSocketEventSessionMessage(BinaryMessage message, String sessionID) {
        super(sessionID);
        this.message = message;
    }
}

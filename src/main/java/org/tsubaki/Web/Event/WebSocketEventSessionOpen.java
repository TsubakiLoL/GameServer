package org.tsubaki.Web.Event;

import org.springframework.web.socket.WebSocketSession;
import org.tsubaki.MessageQueue.MessageEvent;

public class WebSocketEventSessionOpen extends WebSocketEventSession {


    public WebSocketEventSessionOpen(String sessionID) {
        super(sessionID);
    }
}

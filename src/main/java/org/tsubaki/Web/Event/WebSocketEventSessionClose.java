package org.tsubaki.Web.Event;

import org.springframework.web.socket.WebSocketSession;
import org.tsubaki.MessageQueue.MessageEvent;

public class WebSocketEventSessionClose  extends WebSocketEventSession {


    public WebSocketEventSessionClose(String sessionID) {
        super(sessionID);
    }
}

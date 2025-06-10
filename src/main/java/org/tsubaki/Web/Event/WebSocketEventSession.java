package org.tsubaki.Web.Event;

import org.springframework.web.socket.WebSocketSession;
import org.tsubaki.MessageQueue.MessageEvent;

public class WebSocketEventSession  extends MessageEvent {


    public String sessionID;

    public WebSocketEventSession(String sessionID){
        this.sessionID=sessionID;
    }

}

package org.tsubaki.Web;


import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.tsubaki.MessageQueue.MessageCallback;
import org.tsubaki.MessageQueue.MessageEvent;
import org.tsubaki.MessageQueue.MessageQueue;
import org.tsubaki.Tool.BidirectionalMap;
import org.tsubaki.Web.Event.*;

import java.io.IOException;


public class WebSocketServer extends BinaryWebSocketHandler {

    BidirectionalMap<String,WebSocketSession> sessionMap=new BidirectionalMap<String,WebSocketSession>();

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        String sessionID=session.getId();
        MessageQueue.m.push_event(new WebSocketEventSessionMessage(message,sessionID));
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionID=session.getId();
        sessionMap.put(sessionID,session);
        MessageQueue.m.push_event(new WebSocketEventSessionOpen(sessionID));
        // 会话属性已在握手时设置
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        String sessionID=session.getId();
        sessionMap.removeByKey(sessionID);
        MessageQueue.m.push_event(new WebSocketEventSessionClose(sessionID));
    }

    public void sendWebSocketMessage(WebSocketSession session,BinaryMessage message) throws IOException {
        session.sendMessage(message);
    }

    public WebSocketServer(){
        MessageCallback onSendRequestCallback = new MessageCallback() {
            @Override
            public void event_get(MessageEvent event) {

                if (event instanceof WebSocketEventSessionMessageSendRequest eventReal) {
                    if (!sessionMap.containsKey(eventReal.sessionID)) {
                        return;
                    }
                    WebSocketSession session = sessionMap.getValue(eventReal.sessionID);
                    try {

                        sendWebSocketMessage(session, eventReal.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        MessageQueue.m.take_in_event(WebSocketEventSessionMessageSendRequest.class, onSendRequestCallback);

        MessageCallback onKickRequestCallback = new MessageCallback() {
            @Override
            public void event_get(MessageEvent event) {

                if (event instanceof WebSocketEventSessionKickRequest eventReal) {
                    if (!sessionMap.containsKey(eventReal.sessionID)) {
                        return;
                    }
                    WebSocketSession session = sessionMap.getValue(eventReal.sessionID);
                    try {
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        };
        MessageQueue.m.take_in_event(WebSocketEventSessionKickRequest.class, onKickRequestCallback);
    }
}
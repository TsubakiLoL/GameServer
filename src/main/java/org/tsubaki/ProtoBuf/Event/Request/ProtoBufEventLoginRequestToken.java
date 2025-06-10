package org.tsubaki.ProtoBuf.Event.Request;

import org.tsubaki.MessageQueue.MessageEvent;

public class ProtoBufEventLoginRequestToken extends MessageEvent {
    String session_id;

    String token;

    public ProtoBufEventLoginRequestToken(String session_id, String token) {
        this.session_id = session_id;
        this.token = token;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ProtoBufEventLoginRequestToken{" +
                "session_id='" + session_id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

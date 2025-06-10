package org.tsubaki.ProtoBuf.Event.Request;

import org.tsubaki.MessageQueue.MessageEvent;

public class ProtoBufEventLoginRequestNormal  extends MessageEvent {
    String session_id;
    String id;
    String password;

    public ProtoBufEventLoginRequestNormal(String session_id, String id, String password) {
        this.session_id = session_id;
        this.id = id;
        this.password = password;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ProtoBufEventLoginRequestNormal{" +
                "session_id='" + session_id + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

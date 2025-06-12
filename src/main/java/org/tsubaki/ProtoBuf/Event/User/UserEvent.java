package org.tsubaki.ProtoBuf.Event.User;

import org.tsubaki.MessageQueue.MessageEvent;

public class UserEvent extends MessageEvent {
    String userID;

    public UserEvent(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

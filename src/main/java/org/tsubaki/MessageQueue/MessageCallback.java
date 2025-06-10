package org.tsubaki.MessageQueue;

import java.util.Map;

public interface MessageCallback {

    abstract void event_get(MessageEvent event);
}

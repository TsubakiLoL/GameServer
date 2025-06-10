package org.tsubaki.MessageQueue;



import org.tsubaki.Tool.ClassTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//消息队列
public class MessageQueue {

    //队列
    Map<Class<? extends MessageEvent>, ArrayList<MessageCallback>> callMap =new HashMap<Class<? extends MessageEvent>, ArrayList<MessageCallback>>();
    public static MessageQueue m =new MessageQueue();


    //发布事件
    public void push_event(MessageEvent event){
        System.out.println("事件推送:"+event.toString());
        List<Class<?>> eventClassList= ClassTool.getClassHierarchy(event.getClass(),MessageEvent.class);
        eventClassList.forEach(eventClass -> {
            if(callMap.containsKey(eventClass)){
                ArrayList<MessageCallback> callbacks =callMap.get(eventClass);
                callbacks.forEach(callback -> {
                    callback.event_get(event);
                });
            }
        });
    }

    //订阅事件
    public void take_in_event(Class<? extends MessageEvent> eventClass,MessageCallback callback){
        if(!callMap.containsKey(eventClass)){
            callMap.put(eventClass,new ArrayList<MessageCallback>());
        }
        ArrayList<MessageCallback> callbacks =callMap.get(eventClass);
        if(!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }
    //取消订阅
    public void let_out_event(Class<? extends MessageEvent> eventClass,MessageCallback callback){
        if(!callMap.containsKey(eventClass)) return;
        ArrayList<MessageCallback> callbacks =callMap.get(eventClass);
        callbacks.remove(callback);
    }




}

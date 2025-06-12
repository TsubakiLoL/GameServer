package org.tsubaki.Game.Loop;

import org.tsubaki.Game.Action.GameAction;
import org.tsubaki.Game.Notice.GameNotice;
import org.tsubaki.Game.Notice.GameNoticeGameObjectSpawn;
import org.tsubaki.Game.Object.GameObject;
import org.tsubaki.Tool.UUIDTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventLoopGame  extends EventLoop{

    //对象字典
    Map<String,GameObject> objectMap=new HashMap<String,GameObject>();


    ArrayList<GameAction> frameActionArray=new ArrayList<GameAction>();
    ArrayList<GameNotice> frameActionNotice=new ArrayList<GameNotice>();
    public EventLoopGame(int fps) {
        super(fps);
    }

    @Override
    void Process(float delta) {
        //加入收集Action的选项



        for (GameObject gameObject:objectMap.values()) {
            gameObject.gameActionGet(frameActionArray);
        }

        exportNotice();
        frameActionArray.clear();
        frameActionNotice.clear();
    }

    @Override
    void Ready() {

    }

    //子类调用进行写入通知
    void pushNotice(GameNotice notice){
        frameActionNotice.add(notice);
    }
    //将通知发送给用户
    void exportNotice(){

    }
    //生成新的Object，返回随机ID
    public String spawnGameObject(String objectType,Map<String,Object> initValueMap){
        Map<String,Class<?extends GameObject>> classMap=GameObject.objectClassDB;

        if(!classMap.containsKey(objectType)){
            return "";
        }
        //随机ID
        String uuid= UUIDTool.generateUUIDWithoutHyphen();
        while (objectMap.containsKey(uuid)){
            uuid=UUIDTool.generateUUIDWithoutHyphen();
        }
        Class<?extends GameObject> objectClass=classMap.get(objectType);

        try {
            GameObject newObj=objectClass.getDeclaredConstructor().newInstance(this,uuid);
            newObj.setInitValue(initValueMap);
            objectMap.put(uuid,newObj);
            pushNotice(new GameNoticeGameObjectSpawn(objectType,uuid,initValueMap));
            return uuid;
        } catch (Exception e){
            return "";
        }
    }
}

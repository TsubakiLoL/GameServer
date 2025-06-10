package org.tsubaki.Game.Notice;

import org.tsubaki.Game.Action.GameAction;
import org.tsubaki.Game.Object.GameObject;

import java.util.ArrayList;
import java.util.Map;

public class GameNoticeGameObjectSpawn extends GameNotice {

    String gameObjectType;

    String gameObjectID;

    Map<String,Object> initValueMap;

    public GameNoticeGameObjectSpawn(String gameObjectType, String gameObjectID, Map<String, Object> initValueMap) {
        this.gameObjectType = gameObjectType;
        this.gameObjectID = gameObjectID;
        this.initValueMap = initValueMap;
    }
}

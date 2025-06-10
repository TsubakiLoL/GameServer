package org.tsubaki.Game.Object.Impl;

import org.tsubaki.Game.Action.GameAction;
import org.tsubaki.Game.Loop.EventLoopGame;
import org.tsubaki.Game.Object.BroadcastChange;
import org.tsubaki.Game.Object.GameObject;
import org.tsubaki.Game.Object.GameObjectType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

@GameObjectType("GameObject3D")
public class GameObject3D extends GameObject {

    @BroadcastChange
    String broadcastValue="";

    public GameObject3D(EventLoopGame eventLoopGame, String objectID) {
        super(eventLoopGame, objectID);
    }

    @Override
    public void gameActionGet(ArrayList<GameAction> actionList) {

    }


}

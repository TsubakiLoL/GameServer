package org.tsubaki.Game.Object;

import org.tsubaki.Game.Action.GameAction;
import org.tsubaki.Game.Loop.EventLoopGame;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GameObject {
    // 全局类型注册表：类型标识符 -> 类对象（静态注册）

    String objectID;
    public static final Map<String, Class<? extends GameObject>> objectClassDB = new ConcurrentHashMap<>();
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    protected final EventLoopGame eventLoopGame;

    public GameObject(EventLoopGame eventLoopGame,String objectID) {
        this.eventLoopGame = eventLoopGame;
        this.objectID=objectID;
        // 自动注册所有带@BroadcastChange注解的属性
        registerAnnotatedFields();
    }

    public abstract void gameActionGet(ArrayList<GameAction> actionList);

    // 添加属性变更监听器
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    // 移除监听器
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    // 自动注册带注解的字段
    private void registerAnnotatedFields() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(BroadcastChange.class)) {
                field.setAccessible(true);
                String propertyName = field.getName();

                // 添加通用属性变更监听
                addPropertyChangeListener(evt -> {
                    if (evt.getPropertyName().equals(propertyName)) {
                        onPropertyBroadcast(propertyName, evt.getOldValue(), evt.getNewValue());
                    }
                });
            }
        }
    }


    // 属性变更时触发的广播方法
    protected void onPropertyBroadcast(String propertyName, Object oldValue, Object newValue) {


    }

    // 带广播通知的setter模板方法
    protected <T> void setProperty(String propertyName, T oldValue, T newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }
    protected <T> void setProperty(String propertyName, T newValue) {
        T oldValue = null;
        try {
            // 获取当前类的Class对象
            Class<?> clazz = this.getClass();
            // 获取指定名称的属性
            Field field = clazz.getDeclaredField(propertyName);
            // 设置可访问私有属性
            field.setAccessible(true);
            // 获取属性的旧值
            oldValue = (T) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // 处理异常，例如记录日志
            e.printStackTrace();
        }

        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    //获取对象类型
    public String getGameObjectType(){
        if(getClass().isAnnotationPresent(GameObjectType.class)) {
            GameObjectType annotation = getClass().getAnnotation(GameObjectType.class);
            return annotation.value();
        }
        return null;
    }

    public static void registerGameObjectType(Class<? extends GameObject> gameObjectClass) {
        // 获取类型注解
        GameObjectType typeAnnotation = gameObjectClass.getAnnotation(GameObjectType.class);
        String typeId = typeAnnotation.value();

        // 检查类型冲突
        if (objectClassDB.containsKey(typeId)) {
            throw new IllegalStateException("重复的游戏对象类型ID: " + typeId);
        }

        // 注册类型
        objectClassDB.put(typeId, gameObjectClass);
    }


    //初始化值
    public void setInitValue(Map<String, Object> initValueMap) {
        if (initValueMap == null || initValueMap.isEmpty()) {
            return;
        }

        Class<?> clazz = this.getClass();

        for (Map.Entry<String, Object> entry : initValueMap.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                continue; // 跳过null值
            }

            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                // 检查类型是否匹配
                if (field.getType().isAssignableFrom(value.getClass())) {
                    field.set(this, value);
                }
            } catch (NoSuchFieldException e) {
                // 类中不存在该字段，跳过
            } catch (IllegalAccessException e) {
                System.err.println("无法访问字段 " + fieldName + ": " + e.getMessage());
            }
        }
    }
}
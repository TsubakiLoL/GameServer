package org.tsubaki.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClassTool {

    /**
     * 获取类的完整继承链（包括自身和所有父类，直到指定的终点类型）
     * @param clazz 目标类
     * @param endType 终点类型（包含），如果为null则遍历到Object
     * @return 从子类到终点类型的继承链列表
     */
    public static List<Class<?>> getClassHierarchy(Class<?> clazz, Class<?> endType) {
        List<Class<?>> hierarchy = new ArrayList<>();
        // 从当前类开始向上遍历直到终点类型
        Class<?> current = clazz;
        while (current != null) {
            hierarchy.add(current);
            // 检查是否到达终点类型
            if (endType != null && current.equals(endType)) {
                break;
            }
            // 检查是否终点类型的子类
            if (endType != null && endType.isAssignableFrom(current) &&
                    current.getSuperclass() != null &&
                    endType.equals(current.getSuperclass())) {
                // 下一级就是终点类型，提前终止
                break;
            }
            current = current.getSuperclass();
        }
        return hierarchy;
    }


}
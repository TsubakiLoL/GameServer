package org.tsubaki.Tool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程安全的双向映射实现，支持通过键查值和通过值查键
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class BidirectionalMap<K, V> {
    private final ConcurrentHashMap<K, V> keyToValueMap;
    private final ConcurrentHashMap<V, K> valueToKeyMap;

    public BidirectionalMap() {
        this.keyToValueMap = new ConcurrentHashMap<>();
        this.valueToKeyMap = new ConcurrentHashMap<>();
    }

    /**
     * 建立键值对映射。如果键或值已存在，会先移除旧映射
     * @param key 键
     * @param value 值
     */
    public synchronized void put(K key, V value) {
        // 移除旧键的映射
        if (keyToValueMap.containsKey(key)) {
            V oldValue = keyToValueMap.get(key);
            valueToKeyMap.remove(oldValue);
        }

        // 移除旧值的映射
        if (valueToKeyMap.containsKey(value)) {
            K oldKey = valueToKeyMap.get(value);
            keyToValueMap.remove(oldKey);
        }

        // 建立新映射
        keyToValueMap.put(key, value);
        valueToKeyMap.put(value, key);
    }

    /**
     * 通过键获取值
     * @param key 键
     * @return 值，如果不存在则返回 null
     */
    public V getValue(K key) {
        return keyToValueMap.get(key);
    }

    /**
     * 通过值获取键
     * @param value 值
     * @return 键，如果不存在则返回 null
     */
    public K getKey(V value) {
        return valueToKeyMap.get(value);
    }

    /**
     * 移除键对应的映射
     * @param key 键
     * @return 被移除的值，如果不存在则返回 null
     */
    public synchronized V removeByKey(K key) {
        V value = keyToValueMap.remove(key);
        if (value != null) {
            valueToKeyMap.remove(value);
        }
        return value;
    }

    /**
     * 移除值对应的映射
     * @param value 值
     * @return 被移除的键，如果不存在则返回 null
     */
    public synchronized K removeByValue(V value) {
        K key = valueToKeyMap.remove(value);
        if (key != null) {
            keyToValueMap.remove(key);
        }
        return key;
    }

    /**
     * 检查是否包含指定键
     * @param key 键
     * @return 如果包含则返回 true
     */
    public boolean containsKey(K key) {
        return keyToValueMap.containsKey(key);
    }

    /**
     * 检查是否包含指定值
     * @param value 值
     * @return 如果包含则返回 true
     */
    public boolean containsValue(V value) {
        return valueToKeyMap.containsKey(value);
    }

    /**
     * 获取键集合（只读视图）
     * @return 键集合
     */
    public Set<K> keySet() {
        return Collections.unmodifiableSet(keyToValueMap.keySet());
    }

    /**
     * 获取值集合（只读视图）
     * @return 值集合
     */
    public Set<V> values() {
        return Collections.unmodifiableSet(valueToKeyMap.keySet());
    }

    /**
     * 获取正向映射的只读视图
     * @return 正向映射
     */
    public Map<K, V> forwardMap() {
        return Collections.unmodifiableMap(keyToValueMap);
    }

    /**
     * 获取反向映射的只读视图
     * @return 反向映射
     */
    public Map<V, K> reverseMap() {
        return Collections.unmodifiableMap(valueToKeyMap);
    }

    /**
     * 获取映射大小
     * @return 映射中键值对的数量
     */
    public int size() {
        return keyToValueMap.size();
    }

    /**
     * 检查映射是否为空
     * @return 如果为空则返回 true
     */
    public boolean isEmpty() {
        return keyToValueMap.isEmpty();
    }

    /**
     * 清空映射
     */
    public synchronized void clear() {
        keyToValueMap.clear();
        valueToKeyMap.clear();
    }

    /**
     * 返回映射的字符串表示形式
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "BidirectionalMap{" +
                "forward=" + keyToValueMap +
                ", reverse=" + valueToKeyMap +
                '}';
    }

    /**
     * 比较对象是否相等
     * @param o 要比较的对象
     * @return 如果对象相等则返回 true
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BidirectionalMap<?, ?> that = (BidirectionalMap<?, ?>) o;
        return keyToValueMap.equals(that.keyToValueMap) &&
                valueToKeyMap.equals(that.valueToKeyMap);
    }

    /**
     * 计算哈希值
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(keyToValueMap, valueToKeyMap);
    }
}    
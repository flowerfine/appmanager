package com.alibaba.tesla.appmanager.common.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;

/**
 * 类工具
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
public class ClassUtil {

    /**
     * 拷贝 object 中的同名变量，从 from 到 to 中
     *
     * @param from 获取来源对象
     * @param to   设置目标对象
     */
    public static void copy(final Object from, final Object to) {
        Map<String, Field> fromFields = analyze(from);
        Map<String, Field> toFields = analyze(to);
        fromFields.keySet().retainAll(toFields.keySet());
        for (Map.Entry<String, Field> fromFieldEntry : fromFields.entrySet()) {
            final String name = fromFieldEntry.getKey();
            final Field sourceField = fromFieldEntry.getValue();
            final Field targetField = toFields.get(name);
            sourceField.setAccessible(true);
            if (Modifier.isFinal(targetField.getModifiers())) {
                continue;
            }
            targetField.setAccessible(true);
            try {
                if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                    targetField.set(to, sourceField.get(from));
                } else if (sourceField.getType().isAssignableFrom(String.class)
                    && targetField.getType().isAssignableFrom(JSONObject.class)) {
                    targetField.set(to, JsonUtil.toJson((String)sourceField.get(from)));
                } else if (sourceField.getType().isAssignableFrom(JSONObject.class)
                    && targetField.getType().isAssignableFrom(String.class)) {
                    targetField.set(to, JsonUtil.toJsonString((JSONObject)sourceField.get(from)));
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Can't access field");
            }
        }
    }

    private static Map<String, Field> analyze(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Map<String, Field> map = new TreeMap<>();
        Class<?> current = object.getClass();
        while (current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!map.containsKey(field.getName())) {
                        map.put(field.getName(), field);
                    }
                }
            }
            current = current.getSuperclass();
        }
        return map;
    }
}

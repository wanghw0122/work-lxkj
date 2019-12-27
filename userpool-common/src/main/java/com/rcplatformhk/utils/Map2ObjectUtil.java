package com.rcplatformhk.utils;

import com.rcplatformhk.annotation.Default;
import com.rcplatformhk.annotation.FieldType;
import com.rcplatformhk.common.MapToObjectException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class Map2ObjectUtil {
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;
        try {
            T obj = beanClass.getDeclaredConstructor().newInstance();

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);

                FieldType[] fieldType = field.getAnnotationsByType(FieldType.class);
                Default[] defaults = field.getAnnotationsByType(Default.class);
                String fieldName = ArrayUtils.isEmpty(fieldType) ? field.getName() : fieldType[0].field();
                String defaultValue = ArrayUtils.isNotEmpty(defaults) ? defaults[0].value() : null;
                field.set(obj, cast2Object(field.getType(), map.getOrDefault(fieldName, defaultValue)));
            }
            ValidationUtils.ValidationResult validationResult = ValidationUtils.validateEntity(obj);
            if (validationResult.isHasErrors()) {
                String msg = validationResult.getErrorMsg().toString();
                throw new MapToObjectException("MapToObjectException Error! Msg: " + msg);
            }
            return obj;
        } catch (Exception e) {
            log.error("Map2ObjectUtil Exception{} ", e.getMessage(), e);
        }
        return null;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast2Object(Class<T> clazz, Object object) throws Exception {
        if (Objects.isNull(object))
            return null;
        String object_str = String.valueOf(object);
        if (String.class.equals(clazz))
            return (T) String.valueOf(object_str);
        if (Integer.class.equals(clazz))
            return (T) Integer.valueOf(object_str);
        if (Boolean.class.equals(clazz))
            return (T) Boolean.valueOf(object_str);
        if (Float.class.equals(clazz))
            return (T) Float.valueOf(object_str);
        if (Long.class.equals(clazz))
            return (T) Long.valueOf(object_str);
        else
            throw new RuntimeException("Class Not Found!  Class: " + clazz.getName());
    }

}

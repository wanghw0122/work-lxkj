package com.rcplatformhk.userpoolserver.utils;

import com.mysql.jdbc.StringUtils;
import com.rcplatformhk.userpoolserver.annotation.Default;
import com.rcplatformhk.userpoolserver.annotation.FieldType;
import com.rcplatformhk.userpoolserver.common.MapToObjectException;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Map2ObjectUtil {
    public static <T> T mapToObject(Map<String, String> map, Class<T> beanClass) throws Exception {
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

                if (ArrayUtils.isEmpty(fieldType)) {
                    if (ArrayUtils.isNotEmpty(defaults))
                        field.set(obj, cast2Object(field.getType(), map.getOrDefault(field.getName(), defaults[0].value())));
                    else
                        field.set(obj, cast2Object(field.getType(), map.getOrDefault(field.getName(), null)));
                } else
                    field.set(obj, cast2Object(field.getType(), map.get(fieldType[0].field())));
            }
            ValidationUtils.ValidationResult validationResult = ValidationUtils.validateEntity(obj);
            if (validationResult.isHasErrors()) {
                //todo log errors
                String msg = validationResult.getErrorMsg().toString();
                throw new MapToObjectException("MapToObjectException Error! Msg: " + msg);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
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
    private static <T> T cast2Object(Class<T> clazz, String str) throws Exception {
        if (StringUtils.isNullOrEmpty(str))
            return null;
        if (String.class.equals(clazz)) {
            return (T) String.valueOf(str);
        } else if (Integer.class.equals(clazz)) {
            return (T) Integer.valueOf(str);
        } else if (Float.class.equals(clazz)) {
            return (T) Float.valueOf(str);
        } else if (Boolean.class.equals(clazz)) {
            return (T) Boolean.valueOf(str);
        }
        throw new RuntimeException("Class Not Found!  Class: " + clazz.getName());
    }
}

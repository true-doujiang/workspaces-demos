package com.yhh.hbao.core.utils;

import com.yhh.hbao.core.annotation.JsonSerializable;
import com.yhh.hbao.core.annotation.PropertiesName;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by yangjj.
 *
 * @DATE 2016/5/9 - 11:25
 * @company WeiMob
 * @description beanUtils 拷贝工具类
 */
@SuppressWarnings("unused")
public class BeanUtils {
    /**
     * 静态化内存操作
     */
    private static final Map<String, PropertyDescriptor> cachePropertiesDescriptor;

    /**
     * 默认忽略的字段名称
     */
    private static final String defaultIgnoreFiledName = "class";


    static {
        cachePropertiesDescriptor = new HashMap<>();
    }


    /**
     * copy 对象
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 需要忽略的property名称
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        Assert.notNull(source, "bean拷贝原对象不能为空!");
        Assert.notNull(target, "bean拷贝目标对象不能为空!");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        } catch (Exception ex) {
                            LogUtils.error(BeanUtils.class, "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                            throw new RuntimeException("bean copy出现异常");
                        }
                    }
                }
            }
        }
    }

    /**
     * copy 对象
     *
     * @param source           原对象
     * @param targetClass      目标对象
     * @param ignoreProperties 需要忽略的property名称
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, String... ignoreProperties) {
        Assert.notNull(source, "bean拷贝原对象不能为空!");
        T target;
        try {
            target = targetClass.newInstance();
        } catch (Exception e) {
            LogUtils.error(BeanUtils.class, "类初始化失败异常:", e);
            throw new RuntimeException("目标字节码初始化失败!");
        }
        copyProperties(source, target, ignoreProperties);
        return target;
    }


    /**
     * copy 对象
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 需要忽略的property名称
     */
    public static void copyPropertiesAddAnn(Object source, Object target, String... ignoreProperties) {
        Assert.notNull(source, "bean拷贝原对象不能为空!");
        Assert.notNull(target, "bean拷贝目标对象不能为空!");
        Class<?> actualEditable = target.getClass();
        Field[] fields = actualEditable.getDeclaredFields();
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        String fieldName;
        Field sourceField;
        boolean isSourceField = false;
        JsonSerializable sourceJsonSerializable = null;
        for (Field field : fields) {
            fieldName = field.getName();
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(actualEditable, fieldName);
            if (propertyDescriptor == null) continue;
            Method writeMethod = propertyDescriptor.getWriteMethod();
            PropertiesName propertiesName = field.getAnnotation(PropertiesName.class);
            if (propertiesName != null) {
                fieldName = propertiesName.name();
            }
            if (writeMethod != null && (ignoreProperties == null || (!ignoreList.contains(fieldName)))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), fieldName);
                if (sourcePd != null) {
                    try {
                        sourceField = source.getClass().getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                        //异常忽略 继续执行
                        continue;
                    }
                    //加入java json 序列化的 方法判断
                    JsonSerializable targetJsonSerializable = field.getAnnotation(JsonSerializable.class);
                    if (sourceField != null) {
                        sourceJsonSerializable = sourceField.getAnnotation(JsonSerializable.class);
                    }

                    Method readMethod = sourcePd.getReadMethod();
                    boolean isOrigin = readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType());
                    boolean isTargetJsonToObject = readMethod != null && readMethod.getReturnType() == String.class && targetJsonSerializable != null;
                    boolean isTargetObjectToJson = readMethod != null && targetJsonSerializable != null && readMethod.getReturnType() == (targetJsonSerializable.requireClass());
                    boolean isSourceJsonToObject = readMethod != null && readMethod.getReturnType() == String.class && sourceJsonSerializable != null;
                    boolean isSourceObjectToJson = readMethod != null && sourceJsonSerializable != null && readMethod.getReturnType() == (sourceJsonSerializable.requireClass());

                    if (isOrigin || isTargetJsonToObject || isTargetObjectToJson || isSourceJsonToObject || isSourceObjectToJson) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            if (isTargetJsonToObject || isSourceJsonToObject) {
                                if (value != null && targetJsonSerializable != null) {
                                    value = FastJsonUtils.jsonToBean(value.toString(), targetJsonSerializable.requireClass());
                                } else if (value != null && sourceJsonSerializable != null) {
                                    value = FastJsonUtils.jsonToBean(value.toString(), sourceJsonSerializable.requireClass());
                                }
                            }

                            if (isTargetObjectToJson || isSourceObjectToJson) {
                                if (value != null) {
                                    value = FastJsonUtils.beanToJson(value);
                                }
                            }
                            writeMethod.invoke(target, value);
                        } catch (Exception ex) {
                            LogUtils.error(BeanUtils.class, "Could not copy property '" + fieldName + "' from source to target", ex);
                            throw new RuntimeException("bean copy出现异常");
                        }
                    }
                }
            }
        }
    }


    /**
     * bean list copy数据
     *
     * @param sourceList  原拷贝对象list
     * @param targetClass 目标对象字节码
     * @param <T>         泛型数据
     * @return 目标对象list
     */
    public static <T> List<T> copyPropertiesList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) return null;
        if (sourceList.isEmpty()) return Collections.emptyList();
        List<T> targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T target = targetClass.newInstance();
                copyProperties(source, target);
                targetList.add(target);
            }
        } catch (Exception e) {
            LogUtils.error(BeanUtils.class, "bean copy 出现异常:", e);
            throw new RuntimeException("bean copy出现异常");
        }
        return targetList;
    }

    /**
     * bean list copy数据
     *
     * @param sourceList  原拷贝对象list
     * @param targetClass 目标对象字节码
     * @param <T>         泛型数据
     * @return 目标对象list
     */
    public static <T> List<T> copyPropertiesListAnno(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) return null;
        if (sourceList.isEmpty()) return Collections.emptyList();
        List<T> targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T target = targetClass.newInstance();
                copyPropertiesAddAnn(source, target);
                targetList.add(target);
            }
        } catch (Exception e) {
            LogUtils.error(BeanUtils.class, "bean copy 出现异常:", e);
            throw new RuntimeException("bean copy出现异常");
        }
        return targetList;
    }


    /**
     * 获取目标对象的  propertyDescriptors 信息
     *
     * @param targetClazz 目标对象的字节码
     * @return 有效 properties描述 list数据
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> targetClazz) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(targetClazz);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            LogUtils.error(BeanUtils.class, "获取目标类字节码的propertiesDescriptors异常", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取某个class的 propertyDescriptor
     *
     * @param clazz        目标class
     * @param propertyName 属性名称
     * @return clazz的propertiesDescriptor
     */
    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {
        try {
            PropertyDescriptor propertyDescriptor = cachePropertiesDescriptor.get(clazz.getName() + propertyName);
            if (propertyDescriptor == null) {
                propertyDescriptor = new PropertyDescriptor(propertyName, clazz);
                cachePropertiesDescriptor.put(clazz.getName() + propertyName, propertyDescriptor);
            }
            return propertyDescriptor;
        } catch (IntrospectionException e) {
            LogUtils.trace(BeanUtils.class, String.format("beanUtils copyProperties not found{%s};clazzName{%s}", propertyName, clazz.getName()));
            return null;
        }
    }

    /**
     * 将bean数据转化成map数据
     *
     * @param bean         bean数据
     * @param isIgnoreNull 是否忽略空值
     * @return 真实的map对象
     */
    public static Map<String, Object> beanHttpToMap(Object bean, boolean isIgnoreNull) {
        Map<String, Object> propertiesMap = new HashMap<>();
        try {
            Class<?> targetClass = bean.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                String filedName = propertyDescriptor.getName();
                if (filedName.equalsIgnoreCase(defaultIgnoreFiledName)) continue;
                PropertiesName propertiesName = targetClass.getDeclaredField(filedName).getAnnotation(PropertiesName.class);
                filedName = propertiesName != null ? propertiesName.name() : filedName;
                Object result = readMethod.invoke(bean);
                if (isIgnoreNull && result == null) continue;
                propertiesMap.put(filedName, result == null ? null : result.toString());
            }
            return propertiesMap;
        } catch (Exception e) {
            LogUtils.error(BeanUtils.class, "使用beanUtils将对象转化成map异常", e);
            throw new RuntimeException("bean copy出现异常");
        }
    }


    /**
     * 将bean数据转化成map数据
     *
     * @param bean         bean数据
     * @param isIgnoreNull 是否忽略空值
     * @return 真实的map对象
     */
    public static Map<String, Object> beanToMap(Object bean, boolean isIgnoreNull) {
        Map<String, Object> propertiesMap = new HashMap<>();
        try {
            Class<?> targetClass = bean.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                String filedName = propertyDescriptor.getName();
                if (filedName.equalsIgnoreCase(defaultIgnoreFiledName)) continue;
                Field field = getFieldName(filedName, targetClass);
                if (field == null) continue;
                PropertiesName propertiesName = field.getAnnotation(PropertiesName.class);
                filedName = propertiesName != null ? propertiesName.name() : filedName;
                Object result = readMethod.invoke(bean);
                if (isIgnoreNull && result == null) continue;
                propertiesMap.put(filedName, result == null ? null : result);
            }
            return propertiesMap;
        } catch (Exception e) {
            LogUtils.error(BeanUtils.class, "使用beanUtils将对象转化成map异常", e);
            throw new RuntimeException("bean copy出现异常");
        }
    }

    /**
     * 获取真实的字段
     *
     * @param filedName   字段名称
     * @param targetClass 目标类
     * @return 真实字段
     */
    private static Field getFieldName(String filedName, Class<?> targetClass) {
        for (; targetClass != Object.class; targetClass = targetClass.getSuperclass()) {
            try {
                return targetClass.getDeclaredField(filedName);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }


    public static <T> T copyMapToBean(Class<T> targetClass, Map<String, Object> reqMap) {
        try {
            String jsonResult = FastJsonUtils.beanToJson(reqMap);
            return FastJsonUtils.jsonToBean(jsonResult, targetClass);
        } catch (Exception e) {
            LogUtils.error(BeanUtils.class, "使用beanUtils将map转化成对象异常", e);
            throw new RuntimeException("bean copy出现异常");
        }
    }

    /**
     * 比较两个对象的属性值是否一致 ps 仅仅支持一级比较
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 忽略属性
     * @return 比较返回的map
     */
    public static Map<String, BeanDiff> compareBean(Object source, Object target, String... ignoreProperties) {
        Assert.notNull(source, "比较原对象不能为空!");
        Assert.notNull(target, "比较目标对象不能为空!");
        Map<String, BeanDiff> resultMap = new HashMap<>();
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        for (PropertyDescriptor targetPd : targetPds) {
            Method targetReadMethod = targetPd.getReadMethod();
            if (targetReadMethod != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method sourceReadMethod = sourcePd.getReadMethod();
                    if (sourceReadMethod != null &&
                            ClassUtils.isAssignable(targetReadMethod.getReturnType(), sourceReadMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(sourceReadMethod.getDeclaringClass().getModifiers())) {
                                sourceReadMethod.setAccessible(true);
                            }
                            Object sourceValue = sourceReadMethod.invoke(source);

                            if (!Modifier.isPublic(targetReadMethod.getDeclaringClass().getModifiers())) {
                                targetReadMethod.setAccessible(true);
                            }
                            Object targetValue = targetReadMethod.invoke(target);
                            //如果目标值为null 则不会进行操作
                            if (targetValue == null) continue;

                            //如果输入 输出是特殊的时间格式 则转换格式
                            if (sourceValue instanceof Date && targetValue instanceof Date) {
                                String sourceDateValue = Utils.dateFormat((Date) sourceValue);
                                String targetDateValue = Utils.dateFormat((Date) targetValue);
                                if (!targetDateValue.equals(sourceDateValue)) {
                                    resultMap.put(sourcePd.getName(), BeanDiff.build(sourceDateValue, targetDateValue));
                                }
                            } else {
                                if (!String.valueOf(sourceValue).equals(String.valueOf(targetValue))) {
                                    resultMap.put(sourcePd.getName(), BeanDiff.build(sourceValue, targetValue));
                                }
                            }


                        } catch (Exception ex) {
                            LogUtils.error(BeanUtils.class, "Could not compare to  property '" + targetPd.getName() + "' from source to target", ex);
                            throw new RuntimeException("bean compare 出现异常");
                        }
                    }
                }
            }
        }
        return resultMap;
    }


    public static <T> T ignoreSetNull(T target, String... ignoreProperties) {
        Assert.notNull(target, "bean拷贝目标对象不能为空!");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && ignoreList != null && ignoreList.contains(targetPd.getName())) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(target.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    try {
                        writeMethod.invoke(target, null);
                    } catch (Exception ex) {
                        LogUtils.error(BeanUtils.class, "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        throw new RuntimeException("bean copy出现异常");
                    }
                }
            }
        }
        return target;
    }
}

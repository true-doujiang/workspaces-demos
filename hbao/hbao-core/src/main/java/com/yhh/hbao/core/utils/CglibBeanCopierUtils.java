package com.yhh.hbao.core.utils;


import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package: com.weimob.saas.crm.common.utils
 * describe: Bean Copy utils
 * /*注：
 (1)相同属性名，且类型不匹配时候的处理，ok，但是未满足的属性不拷贝；
 (2)get和set方法不匹配的处理，创建拷贝的时候报错，无法拷贝任何属性(当且仅当sourceClass的get方法超过set方法时出现)
 (3)BeanCopier
 初始化例子：BeanCopier copier = BeanCopier.create(Source.class, Target.class, useConverter=true)
 第三个参数userConverter,是否开启Convert,默认BeanCopier只会做同名，同类型属性的copier,否则就会报错.
 copier = BeanCopier.create(source.getClass(), target.getClass(), false);
 copier.copy(source, target, null);
 (4)修复beanCopier对set方法强限制的约束
 改写net.sf.cglib.beans.BeanCopier.Generator.generateClass(ClassVisitor)方法
 将133行的
 MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
 预先存一个names2放入
 109        Map names2 = new HashMap();
 110        for (int i = 0; i < getters.length; ++i) {
 111          names2.put(setters[i].getName(), getters[i]);
 }
 调用这行代码前判断查询下，如果没有改writeMethod则忽略掉该字段的操作，这样就可以避免异常的发生。
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 1/9/18
 * creat_time: 3:55 PM
 **/
public class CglibBeanCopierUtils {

    /**
     *转换缓存
     */
    private static Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

    /**
     * @Title: copyProperties
     * @Description: bean属性转换
     * @param source 资源类
     * @param target  目标类
     */
    public static void copyProperties(Object source,Object target,Converter converter){
        String beanKey = generateKey(source.getClass(),target.getClass());
        BeanCopier copier = null;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), null!=converter);
            beanCopierMap.put(beanKey, copier);
        }else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, converter);
    }
    /**
     * @Title: copyProperties
     * @Description: bean属性转换
     * @param source 资源类
     * @param target  目标类
     */
    public static void copyProperties(Object source,Object target){
        copyProperties(source,target,null);
    }
    /**
     * @Title: copyProperties
     * @Description: bean属性转换
     * @param source 资源类
     * @param clazz  目标CLASS
     */
    public static <T> T copyProperties(Class<T> clazz,Object source){
        return copyProperties(clazz,source,null);
    }
    /**
     * @Title: copyProperties
     * @Description: bean属性转换
     * @param source 资源类
     * @param clazz  目标CLASS
     * @param converter  自定义转换
     */
    public static <T> T copyProperties(Class<T> clazz,Object source,Converter converter){
        T t = null;
        if(null != source){
            try {
                t = clazz.newInstance();
                copyProperties(source,t,converter);
                return t;
            } catch (InstantiationException e) {
                LogUtils.error(CglibBeanCopierUtils.class, e.getMessage());
            } catch (IllegalAccessException e) {
                LogUtils.error(CglibBeanCopierUtils.class, e.getMessage());
            }
        }
        return t;
    }
    private static String generateKey(Class<?>class1,Class<?>class2){
        return class1.toString() + class2.toString();
    }

    /****
     * VO,Dto,PO  List互转
     * @param source
     * @param clazz
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T,S> List<T> copyList(Class<T> clazz,List<S> source){
        List<T> list = new ArrayList<>(source.size());
        for(S s:source){
            T t = copyProperties(clazz,s);
            list.add(t);
        }
        return list;
    }
}

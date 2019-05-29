package com.yhh.hbao.web.service;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.model.ResultAndPage;
import com.yhh.hbao.core.utils.BeanUtils;
import com.yhh.hbao.core.utils.PageUtils;
import com.yhh.hbao.core.utils.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yangjj.
 *
 * @DATE 2017/12/29 - 17:36
 * @company WeiMob
 * @description 基础服务端的实现
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 根据前端业务数据封装 分页请求数据
     * <p>
     * 根据主键分页数据设置
     * </p>
     *
     * @param pageDto       分页对象
     * @param entityWrapper 实体包装对象
     * @return 分页结果数据
     */
    public <E> ResultAndPage selectResultAndPage(PageDto pageDto, Wrapper<T> entityWrapper, Class<E> targetClazz) {
        Page<T> page = new Page<>(pageDto.getPage(), pageDto.getPageSize());
        List<E> dataList = selectPageListByWrapper(page, entityWrapper, targetClazz);
        return new ResultAndPage(dataList, PageUtils.getPageDto(page));
    }
    /**
     * 根据前端业务数据封装 分页请求数据
     * <p>
     * 根据主键分页数据设置
     * </p>
     *
     * @param pageDto       分页对象
     * @param entityWrapper 实体包装对象
     * @return 分页结果数据
     */
    public <E> ResultAndPage selectResultAndPage(PageDto pageDto, Wrapper<T> entityWrapper, Class<E> targetClazz, Map<String, Boolean> orderByKeyMap) {
        Page<T> page = new Page<>(pageDto.getPage(), pageDto.getPageSize());
        List<E> dataList = selectPageListByWrapper(page, entityWrapper, targetClazz,orderByKeyMap);
        return new ResultAndPage(dataList, PageUtils.getPageDto(page));
    }

    /**
     * 特殊分页返回list数据
     *
     * @param page        page对象
     * @param wrapper     包装类
     * @param targetClazz 返回list 目标类型
     * @param <E>         参数
     * @return list数据
     */
    protected <E> List<E> selectPageListByWrapper(Page<T> page, Wrapper<T> wrapper, Class<E> targetClazz) {
        Map<String, Boolean> orderByMap = new HashMap<>();
        orderByMap.put("id", false);
        return selectPageListByWrapper(page, wrapper, targetClazz, orderByMap);
    }


    /**
     * 特殊分页返回list数据
     *
     * @param page          page对象
     * @param wrapper       包装类
     * @param targetClazz   返回list 目标类型
     * @param orderByKeyMap orderByKey的数据
     * @param <E>           参数
     * @return list数据
     */
    protected <E> List<E> selectPageListByWrapper(Page<T> page, Wrapper<T> wrapper, Class<E> targetClazz, Map<String, Boolean> orderByKeyMap) {
        for (Map.Entry<String, Boolean> entry : orderByKeyMap.entrySet()) {
            wrapper.orderBy(entry.getKey(), entry.getValue());
        }
        List<Map<String, Object>> resultList = baseMapper.selectMapsPage(page, wrapper.setSqlSelect("id"));
        if (!Utils.isBlank(resultList)) {
            List<Long> idList = resultList.stream().map(result -> (Long) result.get("id")).collect(Collectors.toList());
            List<T> realList = selectListByIds(idList, orderByKeyMap);
            if (!Utils.isBlank(realList)) {
                return BeanUtils.copyPropertiesList(realList, targetClazz);
            }
        }
        return null;
    }


    /**
     * 根据前端业务数据封装 分页请求数据
     *
     * @param pageDto       分页对象
     * @param entityWrapper 实体包装对象
     * @return 分页结果数据
     */
    public <E> ResultAndPage selectResultAndPageNoId(PageDto pageDto, Wrapper<T> entityWrapper, Class<E> targetClazz) {
        Page<T> page = new Page<>(pageDto.getPage(), pageDto.getPageSize());
        List<E> dataList = selectPageListByWrapperNoId(page, entityWrapper, targetClazz);
        return new ResultAndPage(dataList, PageUtils.getPageDto(page));
    }


    /**
     * 不使用特殊的分页请求
     *
     * @param page        分页数据
     * @param wrapper     实体包装数据
     * @param targetClazz 转化的clazz
     * @param <E>         类型
     * @return 返回列表数据
     */
    protected <E> List<E> selectPageListByWrapperNoId(Page<T> page, Wrapper<T> wrapper, Class<E> targetClazz) {
        List<T> resultList = baseMapper.selectPage(page, wrapper);
        if (!Utils.isBlank(resultList)) {
            return BeanUtils.copyPropertiesList(resultList, targetClazz);
        }
        return null;
    }

    protected List<T> selectListByIds(List<Long> ids) {
        Map<String, Boolean> orderByKeys = new HashMap<>();
        orderByKeys.put("id", false);
        return selectListByIds(ids, orderByKeys);
    }

    /**
     *
     * @param ids
     * @param orderByKeys
     * @return
     */
    protected List<T> selectListByIds(List<Long> ids, Map<String, Boolean> orderByKeys) {
        Wrapper<T> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);
        if (!Utils.isBlank(orderByKeys)) {
            for (Map.Entry<String, Boolean> entry : orderByKeys.entrySet()) {
                wrapper.orderBy(entry.getKey(), entry.getValue());
            }
        }
        return baseMapper.selectList(wrapper);
    }

    /****
     * 查询Model 转Dto
     * @param wrapper
     * @param targetClazz
     * @param <E>
     * @return
     */
    protected <E> List<E> selectList(Wrapper<T> wrapper, Class<E> targetClazz) {
        List<T> list = this.selectList(wrapper);
        if(null!=list&&list.size()>0){
            return BeanUtils.copyPropertiesList(list,targetClazz);
        }
        return null;
    }
}

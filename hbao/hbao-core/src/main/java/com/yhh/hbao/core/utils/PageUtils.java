package com.yhh.hbao.core.utils;


import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.core.model.PageDto;

/**
 * Created by yhh.
 *
 * @DATE 2018/1/3 - 16:38
 * @company WeiMob
 * @description
 */
public class PageUtils {
    public static PageDto getPageDto(Page page) {
        PageDto crmPage = new PageDto();
        crmPage.setPage(page.getCurrent());
        crmPage.setPageSize(page.getSize());
        crmPage.setTotalCount(page.getTotal());
        crmPage.setPageCount(page.getPages());
        return crmPage;
    }

    public static <T> Page<T> getPageByPageDto(PageDto pageDto) {
        Page<T> page = new Page<>();
        page.setCurrent(pageDto.getPage() < 1 ? 1 : pageDto.getPage());
        page.setSize((pageDto.getPageSize() > 100 ? 100 : pageDto.getPageSize()) < 1 ? 1 : pageDto.getPageSize());
        return page;
    }

}

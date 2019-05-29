package com.yhh.hbao.web.service;

import com.yhh.hbao.api.service.CampaignService;
import com.yhh.hbao.api.service.CouponInfoService;
import com.yhh.hbao.api.transfer.CampaignDto;
import com.yhh.hbao.api.transfer.CouponInfoDto;
import com.yhh.hbao.core.enums.CouponStatusEnum;
import com.yhh.hbao.core.constants.Constants;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.DateUtils;
import com.yhh.hbao.core.utils.ReadExcelUtils;
import com.yhh.hbao.orm.mapper.CouponInfoMapper;
import com.yhh.hbao.orm.model.Campaign;
import com.yhh.hbao.orm.model.CouponInfo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.bean.paymch.Coupon;

import java.util.*;

import static com.yhh.hbao.core.enumerate.CouponBizExceptionEnum.COUPON_INFO_CODE_EXHAUSTED;
import static com.yhh.hbao.core.enumerate.CouponBizExceptionEnum.USER_COUPON_RECORD_NOT_FOUND;


/**
 * <p>
 * 卡券信息 服务实现类
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Service
public class CouponInfoServiceImpl extends BaseServiceImpl<CouponInfoMapper,CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponInfoMapper couponInfoMapper;

    @Autowired
    private CampaignService campaignService;

    @Override
    public Page<CouponInfoDto> selectPage(PageDto page, CouponInfoDto couponInfo) {
//        List<Campaign> selectListByParam = campaignMapper.selectList(new EntityWrapper<Campaign>().like(campaign.getGiftName() != null,"gift_name",campaign.getGiftName())
//                .eq(campaign.getCouponType() != null,"coupon_type",campaign.getCouponType()));
//        Page<CampaignDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
//        List<CampaignDto> target = new ArrayList<CampaignDto>();
//        target =  com.yhh.hbao.core.utils.BeanUtils.copyPropertiesList(selectListByParam,CampaignDto.class);
//        pageDto.setRecords(target);
//        return pageDto;
        return  null;
    }

    @Override
    public CouponInfoDto selectById(Long id) {
        CouponInfo couponInfo = super.selectById(id);
        CouponInfoDto dto = couponInfo.toDto(CouponInfoDto.class);
        return dto;
    }

    @Override
    public Boolean updateById(CouponInfoDto couponInfo) {
        return super.updateById(couponInfo.toModel(CouponInfo.class));
    }

    @Override
    public CouponInfoDto insert(CouponInfoDto couponInfo) {
//        return super.insert(couponInfo.toModel(CouponInfo.class));
        return null;
    }

    @Override
    public Integer deleteById(Long id) {
        return null;
    }

    /**
     * c端通过id查询卡券信息
     * @param id
     * @return
     */
    @Override
    public CouponInfoDto selectByIdToc(Long id) {
        CouponInfo couponInfo = this.selectOne(new EntityWrapper<CouponInfo>().eq("id",id));
        if (couponInfo == null){
            throw new CouponBizException(USER_COUPON_RECORD_NOT_FOUND);
        }
        CouponInfoDto dto = new CouponInfoDto();
        BeanUtils.copyProperties(couponInfo,dto);
        return dto;
    }

    /**
     * 通过参数查询卡券列表
     * @param page
     * @param couponInfo
     * @return
     */
    @Override
    public Page<CouponInfoDto> selectPageToc(PageDto page, CouponInfoDto couponInfo) {
        return null;
    }

    /****
     *
     * @param campaignId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public CouponInfoDto getNxCoupon(Long campaignId) {
        Wrapper<CouponInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("status",CouponStatusEnum.NO_ACTIVATION.getValue());
        wrapper.eq("campaign_id",campaignId);
        CouponInfo couponInfo = this.selectOne(wrapper);
        // 如果没有查询到可兑换卡劵劵码
        if(couponInfo == null) {
            throw new CouponBizException(COUPON_INFO_CODE_EXHAUSTED);
        }
        CouponInfo update = new CouponInfo();
        update.setId(couponInfo.getId());
        update.setStatus(CouponStatusEnum.LOCK.getValue());
        this.updateById(update);
        return couponInfo.toDto(CouponInfoDto.class);
    }

    @Override
    @Transactional
    public String insertFromExcel(String fileName) throws Exception {
        StringBuilder errorString = new StringBuilder("ERROR INFO:");
        ReadExcelUtils excelUtils = new ReadExcelUtils(fileName);
        Map<Integer, Map<Integer,Object>> map = excelUtils.readExcelContent();

        Boolean isFirstRow = true;

        List<CouponInfo> entityList = new ArrayList<CouponInfo>();
        //TODO 这里得换成批量插入
        for (Map.Entry<Integer, Map<Integer,Object>> entry : map.entrySet()) {

            if(isFirstRow) {
                isFirstRow = false;
                continue;
            }
            Map<Integer,Object> valueMap = entry.getValue();
            String couponCode = (String) valueMap.get(0);   //第一行传入卡券code
            Long campaignId = Long.parseLong((String)valueMap.get(1)) ;       //第二行传入活动id

            CampaignDto recordDto = campaignService.selectById(campaignId);
            if (recordDto == null){
                errorString.append("第"+entry.getKey()+"活动不存在,");
                continue;
            }

            CouponInfo entity = new CouponInfo();
            entity.setCampaignId(campaignId);

            //避免重复，每次通过查询coupon来确认是否重复，如果重复，拼装成一个列表返回给前台
            //剩下的要求继续执行
            CouponInfo couponInfoRecord = this.selectOne(new EntityWrapper<CouponInfo>().eq("coupon_code",couponCode));
            if (couponInfoRecord != null){
                errorString.append("第"+entry.getKey()+"券码已存在");
                continue;
            }
            entity.setCouponCode(couponCode);
            entity.setGetTime(new Date());

            //constans用枚举
            entity.setInvalidTime(recordDto.getValidEndTime());
            entity.setSource(Constants.COUPON_SOURCE_OTHER);
            entity.setIsVerification(Constants.COUPON_IS_VERIFICATION_NO);
            Date d = new Date();
            entity.setCreateTime(d);

            entityList.add(entity);
        }
        this.insertBatch(entityList,30);
        return errorString.toString();
    }

    @Override
    public List<List<String>> uploadCoupon(String file) throws Exception {

        ReadExcelUtils excelUtils = new ReadExcelUtils(file);
        Map<Integer, Map<Integer,Object>> map = excelUtils.readExcelContent();

        //用于记录导入结果
        List<List<String>> result = new ArrayList<>();

        List<CouponInfo> couponList = new ArrayList<>();
        boolean firstRow = true;
        for (Map.Entry<Integer, Map<Integer, Object>> entry : map.entrySet()) {
            if (firstRow) {
                firstRow = false;
                continue;
            }

            Map<Integer, Object> rowMap = entry.getValue();
            String couponCode = (String) rowMap.get(0);
            String campaignIdStr = (String) rowMap.get(1);
            Long campaignId = Long.valueOf(campaignIdStr);

            //判断活动是否存在
            CampaignDto exitCamp = null;
            try {
                exitCamp = campaignService.selectById(campaignId);
            } catch (CouponBizException e) {
                List<String> resultList = new ArrayList<>();
                resultList.add(couponCode);
                resultList.add(campaignIdStr);
                resultList.add("导入失败  活动id不存在");
                result.add(resultList);
                continue;
            }

            //判断卷码是否已经存在
            CouponInfo exitCoupon = this.selectOne(new EntityWrapper<CouponInfo>().eq("coupon_code", couponCode));
            if (exitCoupon != null) {
                List<String> resultList = new ArrayList<>();
                resultList.add(couponCode);
                resultList.add(campaignIdStr);
                resultList.add("导入失败  该券码已存在");
                result.add(resultList);
                continue;
            }

            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            CouponInfo coupon = new CouponInfo();
            coupon.setCampaignId(campaignId);
            coupon.setCouponCode(couponCode);
            coupon.setGetTime(now);
            coupon.setInvalidTime(exitCamp.getValidEndTime());
            coupon.setSource(Constants.COUPON_SOURCE_OTHER);
            coupon.setIsVerification(Constants.COUPON_IS_VERIFICATION_NO);
            coupon.setCreateTime(now);
            couponList.add(coupon);
        }
        if (couponList.size() > 0) {
            this.insertBatch(couponList, 30);
        }
        return result;
    }

    @Override
    public HSSFWorkbook downloadCoupon(List<List<String>> param) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("优惠券导入结果");

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"优惠券号码", "活动id", "是否成功"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (List<String> list : param) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(list.get(0));
            row1.createCell(1).setCellValue(list.get(1));
            row1.createCell(2).setCellValue(list.get(2));
            rowNum++;
        }
        return workbook;
    }
}

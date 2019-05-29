package com.yhh.hbao.web.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.yhh.hbao.api.service.*;
import com.yhh.hbao.api.transfer.*;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.exception.CouponBizException;
import com.yhh.hbao.core.model.PageDto;
import com.yhh.hbao.core.utils.BeanUtils;
import com.yhh.hbao.core.utils.HttpUtil;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.vo.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/tob")
public class TobController extends BaseController {

    @Autowired
    private BrokenLogsService brokenLogsService;
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private ReceiveLogsService receiveLogsService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserInfoService userInfoService;


    @Value("${user.center.api}")
    private String userCenterApi;

    /**
     * 分页查询拆券记录
     * @return
     */
    @GetMapping(value = "/brokenLog")
    public ResultResponse brokenLoglist(PageDto page, BrokenLogsVo brokenLogsVo) {
        BrokenLogsDto brokenLogs=new BrokenLogsDto();
        BeanUtils.copyProperties(brokenLogsVo,brokenLogs);
        return ResultResponse.success(brokenLogsService.selectPage(page,brokenLogs));
    }

    /**
     * 根据ID查询拆券记录
     * @return
     */
    @GetMapping(value = "/brokenLog/{id}")
    public ResultResponse selectBrokenLogById(@PathVariable("id") Long id) {
        return ResultResponse.success(brokenLogsService.selectById(id));
    }

    //----------------------
    /**
     * 分页查询活动信息
     * @return
     */
    @GetMapping("/campaign/list")
    public ResultResponse campaignList(PageDto page, CampaignVo campaignVo) {
        CampaignDto campaign=new CampaignDto();
        BeanUtils.copyProperties(campaignVo,campaign);
        try{
            campaignVo.setBeginTime(campaign.getBeginTime().toString());
            campaignVo.setEndTime(campaign.getEndTime().toString());
            campaignVo.setValidBeginTime(campaign.getValidBeginTime().toString());
            campaignVo.setValidEndTime(campaign.getValidEndTime().toString());
        } catch (Exception e){

        }
        return ResultResponse.success(campaignService.selectPage(page,campaign));
    }

    /**
     * 根据ID查询活动信息
     * @return
     */
    @GetMapping(value = "/campaign/{id}")
    public ResultResponse campaignSelectById(@PathVariable("id") Long id) {
        return ResultResponse.success(campaignService.selectById(id));
    }

    /**
     * 修改活动信息
     * @return
     */
    @PutMapping(value = "/campaign")
    public ResultResponse updateCampaignById(@RequestBody CampaignVo campaignVo) {
        CampaignDto campaign=new CampaignDto();
        BeanUtils.copyProperties(campaignVo,campaign);
        return ResultResponse.success(campaignService.updateById(campaign));
    }

    /**
     * 保存活动信息
     * @return
     */
    @PostMapping(value = "/campaign")
    public ResultResponse campaignSave(@RequestBody CampaignVo campaignVo) {
        CampaignDto campaign=new CampaignDto();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            campaign.setBeginTime(sdf.parse(campaignVo.getBeginTime()));
            campaign.setEndTime(sdf.parse(campaignVo.getEndTime()));
            campaign.setValidBeginTime(sdf.parse(campaignVo.getValidBeginTime()));
            campaign.setValidEndTime(sdf.parse(campaignVo.getValidEndTime()));
        } catch (Exception e){

        }
        BeanUtils.copyProperties(campaignVo,campaign);
        return ResultResponse.success(campaignService.insert(campaign));
    }

    /**
     * 根据id删除活动（逻辑删除）
     * @param id
     * @return
     */
    @DeleteMapping(value = "/campaign/{id}")
    public ResultResponse campaignDelete(@PathVariable(value = "id") Long id) {
        return  ResultResponse.success(campaignService.deleteById(id));
    }


    //--------------------
    /**
     * 客服通过excel上传卡券util
     * @return
     */
    @PostMapping(value="/byexcel")
    public ResultResponse saveByExcel(HttpServletRequest request,
                                      @RequestParam("file") MultipartFile file) throws  Exception {

        File file1 = null;
        // 如果文件不为空，写入上传路径
//        MultipartFile file = files[0];
        try{

            if(!file.isEmpty()) {
                //上传文件路径
                String path = request.getServletContext().getRealPath("/excel/");
                //上传文件名
                String filename = file.getOriginalFilename();
                File filepath = new File(path,filename);
                //判断路径是否存在，如果不存在就创建一个
                if (!filepath.getParentFile().exists()) {
                    filepath.getParentFile().mkdirs();
                }
                //将上传文件保存到一个目标文件当中
                String finalFileName = path + File.separator + filename;
                file1 = new File(finalFileName);
                file.transferTo(file1);
                String errorMsg = couponInfoService.insertFromExcel(finalFileName);
                return ResultResponse.success(errorMsg);
                //弄完后删除
            } else {
                return ResultResponse.error(-1,"上传文件为空");
            }
        } catch(Exception e){
            throw e;
            //return ResultResponse.error(-1,e.getMessage()+e.getStackTrace());
        }finally {
            if (file1 != null){
                file1.delete();
            }
        }
    }

    /**
     * 导入卡卷
     * @param req
     * @param res
     * @param file
     * @throws Exception
     */
    @PostMapping(value = "/uploadCoupon")
    public void uploadCoupon(HttpServletRequest req, HttpServletResponse res, @RequestParam(value = "file") MultipartFile file) throws Exception {
        File file2 = null;
        try {
            if (!file.isEmpty()) {
                String path = req.getServletContext().getRealPath("/excel/");
                String filename = file.getOriginalFilename();

                File file1 = new File(path, filename);
                File parentFile = file1.getParentFile();
                boolean exists = parentFile.exists();
                if (!exists) {
                    parentFile.mkdirs();
                }
                String finalFilePath = path + File.separator + filename;
                file2 = new File(finalFilePath);
                file.transferTo(file2);

                //导入卡券
                List<List<String>> result = couponInfoService.uploadCoupon(finalFilePath);

                //导出导入卡券结果
                String fileName = "优惠券导入结果"  + ".xls";
                HSSFWorkbook workbook = couponInfoService.downloadCoupon(result);
                res.setContentType("application/octet-stream");
                res.setHeader("Content-disposition", "attachment;filename=" + fileName);
                res.flushBuffer();
                workbook.write(res.getOutputStream());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (file2 != null){
                file2.delete();
            }
        }
    }

    /**
     * 卡券导入模板下载
     * @param res
     */
    @GetMapping(value = "/downloadTemplate")
    public void downloadTemplate(HttpServletResponse res) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            HSSFRow row = sheet.createRow(0);
            HSSFCell couponCodeCell = row.createCell(0);
            couponCodeCell.setCellValue("优惠券号码(coupon_code");
            HSSFCell campaignIdCell = row.createCell(1);
            campaignIdCell.setCellValue("活动id");
            res.setContentType("application/octet-stream");
            res.setHeader("Content-disposition", "attachment;filename=" + new String("优惠券导入模板".getBytes(),"iso-8859-1") + ".xls");
            OutputStream ouputStream = res.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //------------------
    /***
     * tob 用户登录
     * @return
     */
    @PostMapping(value="/tob/login")
    public ResultResponse loginTob(String username,String password){
        if("diandainfo".equals(username) && password.equals("123123")){
            return ResultResponse.success();
        }
        return ResultResponse.error();
    }

    //--------------------
    /**
     * 分页查询领取记录  这里查usercoupon表
     * @return
     */
    @GetMapping(value = "/receiveLogs")
    public ResultResponse receiveLogsList(PageDto page, ReceiveLogsVo receiveLogsVo) {
        ReceiveLogsDto dto = receiveLogsVo.toDto();
//        if (receiveLogsVo.getCouponType() == null){
        Integer p = page.getPage();
        Integer size =page.getPageSize();
        dto.setPage(p);
        dto.setPageSize(size);
        List<ReceiveLogsDto> list1 = receiveLogsService.selectListByparam( dto);
//            List<UserCouponDto> list2= userCouponService.selectListByparam(dto.toVo(UserCouponDto.class));
//            for(UserCouponDto userCouponDto : list2){
//                list1.add(userCouponDto.toVo(ReceiveLogsDto.class));
//            }
        Page<ReceiveLogsDto> pageDto = new Page<>(page.getPage(), page.getPageSize());
        pageDto.setRecords(list1);
        return ResultResponse.success(pageDto);
//        }
//        else if (receiveLogsVo.getCouponType() == 1){//如果是红包，通过receivelog + 活动表查询
////          receiveLogsService.select
//            return ResultResponse.success(receiveLogsService.selectPageByparam( page, dto));
//        } else {
//
//            //如果是代金券（或兑换券，暂时没有），通过usercoupon关联coupon查询
//            return ResultResponse.success(userCouponService.selectPageByparam(page,dto.toModel(UserCouponDto.class)));
//        }
    }

    /**
     * 根据ID查询领取记录
     * @return
     */
    @GetMapping(value = "/receiveLogs/{id}")
    public ResultResponse selectReceiveLogsById(@PathVariable("id") Long id) {
//        if (couponType == 1){
        ReceiveLogsDto dto = receiveLogsService.selectExtById(id);
        if (null == dto){
            throw new CouponBizException(CouponBizExceptionEnum.RECEIVELOGS_IS_NOT_FOUND);
        }
        return ResultResponse.success(dto);
//        } else  {
//            UserCouponDto dto = userCouponService.selectExtById(id);
//            if (null == dto){
//                throw new CouponBizException(CouponBizExceptionEnum.USER_COUPON_RECORD_NOT_FOUND);
//            }
//            return ResultResponse.success(dto);
//        }
    }

    //-------------------
    /**
     * 分页查询用户卡券信息
     * @return
     */
    @GetMapping(value = "/userCoupon/list")
    public ResultResponse userCouponList(PageDto page, UserCouponVo userCouponVo) {
        UserCouponDto userCoupon=new UserCouponDto();
        BeanUtils.copyProperties(userCouponVo,userCoupon);
        return ResultResponse.success(userCouponService.selectPage(page,userCoupon));
    }

    /**
     * 根据ID查询用户卡券信息
     * @return
     */
    @GetMapping(value = "/userCoupon/{id}")
    public ResultResponse selectUserCouponById(@PathVariable("id") Long id) {
        return ResultResponse.success(userCouponService.selectById(id));
    }

    //----------------------------
    /**
     * 分页查询用户信息表
     * @return
     */
    @GetMapping(value = "/userInfo")
    public ResultResponse userInfoList(PageDto page, UserInfoVo userInfoVo) {
        UserInfoDto userInfo=new UserInfoDto();
        BeanUtils.copyProperties(userInfoVo,userInfo);
        return ResultResponse.success(userInfoService.selectPage(page,userInfo));
    }

    /**
     * 根据ID查询用户信息表
     * @return
     */
    @GetMapping(value = "/userInfo/{id}")
    public ResultResponse selectUserInfoById(@PathVariable("id") Long id) {
        ResultResponse response = HttpUtil.doGet(userCenterApi+id, null, ResultResponse.class);
        UserInfoDto userInfoDto = JSON.parseObject(response.getData().toString(), UserInfoDto.class);
        return ResultResponse.success(userInfoDto);
        //return ResultResponse.success(userInfoService.selectById(id));
    }

    /**
     * 修改用户信息表
     * @return
     */
    @PutMapping(value = "/userInfo")
    public ResultResponse updateUserInfoById(@RequestBody UserInfoVo userInfoVo) {
        UserInfoDto userInfo=new UserInfoDto();
        BeanUtils.copyProperties(userInfoVo,userInfo);
        return ResultResponse.success(userInfoService.updateById(userInfo));
    }

    /**
     * 保存用户信息表
     * @return
     */
    @PostMapping(value = "/userInfo")
    public ResultResponse userInfoSave(@RequestBody UserInfoVo userInfoVo) {
        UserInfoDto userInfo=new UserInfoDto();
        BeanUtils.copyProperties(userInfoVo,userInfo);
        return ResultResponse.success(userInfoService.insert(userInfo));
    }



}


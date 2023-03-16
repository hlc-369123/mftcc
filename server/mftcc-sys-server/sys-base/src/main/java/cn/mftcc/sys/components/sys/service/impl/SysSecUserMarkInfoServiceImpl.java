/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.DateUtil;
import cn.mftcc.sys.components.sys.entity.SysSecAuditConfigEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.mapper.SysUserMapper;
import cn.mftcc.sys.components.sys.service.SysSecAuditConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import cn.mftcc.sys.components.sys.entity.SysSecUserMarkInfoEntity;
import cn.mftcc.sys.components.sys.mapper.SysSecUserMarkInfoMapper;
import cn.mftcc.sys.components.sys.service.SysSecUserMarkInfoService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 * 用户记录信息
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-04 15:12:09
 */
@Service("sysSecUserMarkInfoService")
@Transactional(rollbackFor = ServiceException.class)
public class SysSecUserMarkInfoServiceImpl implements SysSecUserMarkInfoService {

    @Autowired
    private SysSecUserMarkInfoMapper sysSecUserMarkInfoMapper;

    @Autowired
    private SysSecAuditConfigService sysSecAuditConfigService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public IPage<SysSecUserMarkInfoEntity> findByPage(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysSecUserMarkInfoEntity> page = new Page<>();
            page.setCurrent(sysSecUserMarkInfoEntity.getPageNo());
            page.setSize(sysSecUserMarkInfoEntity.getPageSize());
            QueryWrapper<SysSecUserMarkInfoEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getOpNo()),"user_id",sysSecUserMarkInfoEntity.getOpNo())
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getPasswordUpdateTime()),"password_update_time",sysSecUserMarkInfoEntity.getPasswordUpdateTime())
                .eq("visit_times",sysSecUserMarkInfoEntity.getVisitTimes())
                .eq("login_error_times",sysSecUserMarkInfoEntity.getLoginErrorTimes())
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getLastSignInTime()),"last_sign_in_time",sysSecUserMarkInfoEntity.getLastSignInTime())
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getLastSignOutTime()),"last_sign_out_time",sysSecUserMarkInfoEntity.getLastSignOutTime())
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getCurrentSignInTime()),"current_sign_in_time",sysSecUserMarkInfoEntity.getCurrentSignInTime())
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getPasswordState()),"password_state",sysSecUserMarkInfoEntity.getPasswordState())
                .eq(StringUtils.isNotBlank(sysSecUserMarkInfoEntity.getPasswordMessege()),"password_messege",sysSecUserMarkInfoEntity.getPasswordMessege());


            return sysSecUserMarkInfoMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }

    @Override
    public void insert(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            sysSecUserMarkInfoMapper.insert(sysSecUserMarkInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }

    @Override
    public void update(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            sysSecUserMarkInfoMapper.updateById(sysSecUserMarkInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }

    @Override
    public SysSecUserMarkInfoEntity findById(String userId) throws ServiceException {
        try{
            return sysSecUserMarkInfoMapper.selectById(userId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,userId,e);
        }
    }

    @Override
    public void deleteById(String userId) throws ServiceException {
        try{
            sysSecUserMarkInfoMapper.deleteById(userId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,userId,e);
        }
    }

    /** 收录用户登录退出信息 从SysLoginLog类 装入 UserMarkInfo类
     * return
     */
    @Override
    public void insertOrUpdate(String opNo,String message) throws ServiceException{
        SysSecUserMarkInfoEntity uMI= this.findById(opNo);
        SysSecAuditConfigEntity sysSecAuditConfigEntity = sysSecAuditConfigService.findById("8");//这句 只为获取密码校验容忍次数
        int itemValues = Integer.parseInt(sysSecAuditConfigEntity.getItemValues());
        if("login".equals(message)){//正常登录
            if(uMI==null){
                uMI = new SysSecUserMarkInfoEntity();
                uMI.setOpNo(opNo);//操作员号
                uMI.setPasswordMessege("正常登录");//登录信息
                uMI.setPasswordState("0");//密码状态
                uMI.setVisitTimes(1);//登录次数
                uMI.setCurrentSignInTime(DateUtil.getDate());//本次登录日期
                this.insert(uMI);
            }else{
                uMI.setLoginErrorTimes(0);//如果正常登录后，历史记录的错误次数清零
                uMI.setPasswordMessege("正常登录");
                uMI.setPasswordState("0");
                uMI.setLastSignInTime(uMI.getCurrentSignInTime());
                uMI.setCurrentSignInTime(DateUtil.getDate());
                uMI.setVisitTimes(uMI.getVisitTimes()+1);
                this.update(uMI);
            }
        }else if("logout".equals(message)){//退出
            if(uMI!=null){
                uMI.setPasswordState("0");
                uMI.setLastSignOutTime(DateUtil.getDate());
                this.update(uMI);
            }
        }else if("updatePassword".equals(message)){//修改密码
            if(uMI!=null){
                uMI.setOpNo(opNo);//操作员号
                uMI.setPasswordMessege("修改密码");//登录信息
                uMI.setPasswordState("0");//密码状态
                uMI.setLoginErrorTimes(0);
                uMI.setPasswordUpdateTime(DateUtil.getDate());
                this.update(uMI);
            }else{
                uMI = new SysSecUserMarkInfoEntity();
                uMI.setOpNo(opNo);//操作员号
                uMI.setPasswordMessege("修改密码");//登录信息
                uMI.setPasswordState("0");//密码状态
                uMI.setLoginErrorTimes(0);
                uMI.setVisitTimes(0);//登录次数
                uMI.setPasswordUpdateTime(DateUtil.getDate());
                this.insert(uMI);
            }
        }else{//密码校验报错
            if(uMI==null){
                uMI = new SysSecUserMarkInfoEntity();
                uMI.setOpNo(opNo);//操作员号
                uMI.setPasswordState("0");//密码状态
                uMI.setVisitTimes(1);//登录次数
                uMI.setCurrentSignInTime(DateUtil.getDate());//本次登录日期
//                uMI.setPasswordUpdateTime(DateUtil.getDate());
                uMI.setLoginErrorTimes(1);
                uMI.setPasswordMessege(message);
                this.insert(uMI);
            }else{
                uMI.setLastSignInTime(uMI.getCurrentSignInTime());
                uMI.setVisitTimes(uMI.getVisitTimes()+1);
                uMI.setCurrentSignInTime(DateUtil.getDate());//本次登录日期
                uMI.setPasswordMessege(message);
                //如果密码锁定状态为“1”，则不修改数据，证明当前密码已锁定无法更改状态
                if(!"1".equals(uMI.getPasswordState())){
                    if((uMI.getLoginErrorTimes())+1-itemValues==0){
                        uMI.setLoginErrorTimes(uMI.getLoginErrorTimes()+1);
                        uMI.setPasswordState("1");//密码错误X次，登录人员锁定不可再登录
                        //修改用户表中状态
                        SysUserEntity sysUserEntity = sysUserMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("op_no", opNo));
                        sysUserEntity.setUserSts("9");
                        sysUserMapper.updateById(sysUserEntity);
                    }else{
                        uMI.setLoginErrorTimes(uMI.getLoginErrorTimes()+1);
                    }
                    this.update(uMI);
                }
            }
        }
    }

    @Override
    public void unlock(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            SysSecUserMarkInfoEntity sysSecUserMarkInfo = new SysSecUserMarkInfoEntity();
            sysSecUserMarkInfo.setOpNo(sysSecUserMarkInfoEntity.getOpNo());
            sysSecUserMarkInfo.setLoginErrorTimes(0);
            sysSecUserMarkInfo.setPasswordState("0");
            sysSecUserMarkInfoMapper.updateById(sysSecUserMarkInfo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }
}
/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysSecAuditConfigEntity;

import cn.mftcc.common.exception.ServiceException;

import java.util.List;

/**
 * 登陆校验规则表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-03 13:42:15
 */
public interface SysSecAuditConfigService {

    IPage<SysSecAuditConfigEntity> findByPage(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException;

    List<SysSecAuditConfigEntity> findList(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException;

    void insert(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException;

    void update(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException;

    SysSecAuditConfigEntity findById(String itemNo) throws ServiceException;

    void deleteById(String itemNo) throws ServiceException;

    JSONObject validatePWAjax(String opNo,String password) throws ServiceException;

    JSONObject SecurityPwd(String opNo,String type) throws ServiceException;

    /**
     *
     * @param length 图片验证码位数
     * @param typeMix 数字和字母混合
     * @param pureNumber 纯数字('number')或者字母('alphabet')
     * @param capsLookMix 字母大小写混合
     * @param numLength 混合类型下的数字个数
     * @param uupperLength 大写字母的个数
     * @return
     * @throws ServiceException
     */
    String createIdentifyCode(int length,boolean typeMix,String pureNumber,boolean capsLookMix,int numLength,int uupperLength) throws ServiceException;
}


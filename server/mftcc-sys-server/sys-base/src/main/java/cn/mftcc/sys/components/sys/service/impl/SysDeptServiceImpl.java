/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.common.constant.PltConstant;
import cn.mftcc.sys.components.sys.entity.SysDeptEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.mapper.SysDeptMapper;
import cn.mftcc.sys.components.sys.service.SysDeptService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("sysDeptService")
@Transactional(rollbackFor=Exception.class)
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RequestUtil requestUtil;

    @Override
    public IPage<SysDeptEntity> findByPage(SysDeptEntity sysDeptEntity) throws ServiceException {
        try {
            //翻页
            IPage<SysDeptEntity> page = new Page<>();
            page.setCurrent(sysDeptEntity.getPageNo());
            page.setSize(sysDeptEntity.getPageSize());
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                String corpId = requestUtil.getUserInfo("corpId").toString();
                queryWrapper.eq(StringUtils.isNotBlank(corpId),"corp_id",corpId);
            }

            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysDeptEntity.getBrName()),"BR_NAME",sysDeptEntity.getBrName())
                    .like(StringUtils.isNotBlank(sysDeptEntity.getBrName()),"FULL_BR_NAME",sysDeptEntity.getBrName())
                    .eq(StringUtils.isNotBlank(sysDeptEntity.getBrSts()),"BR_STS",sysDeptEntity.getBrSts())
                    .eq(StringUtils.isNotBlank(sysDeptEntity.getBrTyp()),"BR_TYP",sysDeptEntity.getBrTyp());
            if(sysDeptEntity.getBrId()!=null&&sysDeptEntity.getBrId()>0){
                Set<Integer> ids = getIds(sysDeptEntity.getBrId());
                queryWrapper.lambda().in(SysDeptEntity::getBrId,ids);
            }
            return sysDeptMapper.selectPage(page,queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysDeptEntity.getBrId()+"",e);
        }

    }

    @Override
    public IPage<SysDeptEntity> findAllByPage(SysDeptEntity sysDeptEntity) throws ServiceException {
        try {
            //翻页
            IPage<SysDeptEntity> page = new Page<>();
            page.setCurrent(sysDeptEntity.getPageNo());
            page.setSize(sysDeptEntity.getPageSize());
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysDeptEntity.getBrName()),"BR_NAME",sysDeptEntity.getBrName())
                    .like(StringUtils.isNotBlank(sysDeptEntity.getBrName()),"FULL_BR_NAME",sysDeptEntity.getBrName())
                    .eq(StringUtils.isNotBlank(sysDeptEntity.getBrSts()),"BR_STS",sysDeptEntity.getBrSts())
                    .eq(StringUtils.isNotBlank(sysDeptEntity.getBrTyp()),"BR_TYP",sysDeptEntity.getBrTyp());
            return sysDeptMapper.selectPage(page,queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysDeptEntity.getBrId()+"",e);
        }

    }

    @Override
    public IPage<SysDeptEntity> findByPage4admin(SysDeptEntity sysDeptEntity) throws ServiceException {
        try {
            //翻页
            IPage<SysDeptEntity> page = new Page<>();
            page.setCurrent(sysDeptEntity.getPageNo());
            page.setSize(sysDeptEntity.getPageSize());
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                List<String> corpId = (List<String>)requestUtil.getUserInfo("corpIdChildren");
                queryWrapper.in(corpId.size()>0,"corp_id",corpId);
            }

            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysDeptEntity.getBrName()),"BR_NAME",sysDeptEntity.getBrName())
                    .like(StringUtils.isNotBlank(sysDeptEntity.getBrName()),"FULL_BR_NAME",sysDeptEntity.getBrName())
                    .eq(StringUtils.isNotBlank(sysDeptEntity.getBrSts()),"BR_STS",sysDeptEntity.getBrSts())
                    .eq(StringUtils.isNotBlank(sysDeptEntity.getBrTyp()),"BR_TYP",sysDeptEntity.getBrTyp());
            if(sysDeptEntity.getBrId()!=null&&sysDeptEntity.getBrId()>0){
                Set<Integer> ids = getIds(sysDeptEntity.getBrId());
                queryWrapper.lambda().in(SysDeptEntity::getBrId,ids);
            }
            return sysDeptMapper.selectPage(page,queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysDeptEntity.getBrId()+"",e);
        }

    }

    @Override
    public List<SysDeptEntity> getAll() throws ServiceException{
        try {
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                List<String> corpId = (List<String>)requestUtil.getUserInfo("corpIdChildren");
                queryWrapper.in(corpId.size()>0,"corp_id",corpId);
            }
            queryWrapper.orderByDesc("is_corp");
            queryWrapper.orderByAsc("br_lvl");
            queryWrapper.orderByAsc("br_no");
            List<SysDeptEntity> list = sysDeptMapper.selectList(queryWrapper);
            //获取root级机构
            int brLvl = 1;
            if(list.size()>0){
                brLvl = list.get(0).getBrLvl();
            }
            queryWrapper.eq("br_lvl",brLvl)
                    .eq("br_sts","1");
            List<SysDeptEntity> firstList = sysDeptMapper.selectList(queryWrapper);
            List<SysDeptEntity> treeList = new ArrayList<SysDeptEntity>();
            for(SysDeptEntity root:firstList){
                treeList.add(findChildren(root, list));
            }
            return treeList;
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }


    @Override
    public List<SysDeptEntity> getList(SysDeptEntity sysDeptEntity) throws ServiceException{
        try {
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<SysDeptEntity>();
            queryWrapper.setEntity(sysDeptEntity);
            List<SysDeptEntity> list = sysDeptMapper.selectList(queryWrapper);
            return list;
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<SysDeptEntity> getListByCorpId() throws ServiceException{
        try {
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                String corpId = requestUtil.getUserInfo("corpId").toString();
                queryWrapper.eq(StringUtils.isNotBlank(corpId),"corp_id",corpId);
            }
            return sysDeptMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public SysDeptEntity findByBrNo(String brNo) {
        try {
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("br_no", brNo);
            return sysDeptMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public SysDeptEntity findByCorpId(String corpId) {
        try {
            QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("corp_id", corpId);
            queryWrapper.eq("is_corp","1");
            return sysDeptMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<String> getChildrenBrNoList(String brNo, String corpId) throws ServiceException {
        try {
            SysDeptEntity sysDeptEntity = new SysDeptEntity();
            sysDeptEntity.setCorpId(corpId);
            List<SysDeptEntity> list = getList(sysDeptEntity);
            List<String> brNoList = new ArrayList<>();
            brNoList.add(brNo);
            for(int i=0;i<list.size();i++){
                if(list.get(i).getUpOne().equals(brNo)){
                    brNoList.add(list.get(i).getBrNo());
                    getChildDept(brNoList,list.get(i),list);
                }
            }
            return brNoList;
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<String> getChildrenCorpIdList(String corpId) throws ServiceException {
        try {
            SysDeptEntity sysDeptEntity = new SysDeptEntity();
            sysDeptEntity.setCorpId(corpId);
            sysDeptEntity.setIsCorp("1");
            List<SysDeptEntity> list = getList(sysDeptEntity);
            String brNo = "";
            int brLvl = 99;
            for(int i=0;i<list.size();i++){
                if(list.get(i).getBrLvl()<brLvl){
                    brNo = list.get(i).getBrNo();
                }
            }
            sysDeptEntity.setCorpId(null);
//            sysDeptEntity.setBrNo(brNo);
            List<String> brNoList = new ArrayList<>();
            brNoList.add(corpId);
            List<SysDeptEntity> list2 = getList(sysDeptEntity);
            for(int i=0;i<list2.size();i++){
                if(list2.get(i).getUpOne().equals(brNo)){
                    if (!brNoList.contains(list2.get(i).getCorpId())) {
                        brNoList.add(list2.get(i).getCorpId());
                    }
                    getChildDept4CorpId(brNoList,list2.get(i),list2);
                }
            }
            return brNoList;
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    private void getChildDept4CorpId(List<String> brNoList, SysDeptEntity sysDeptEntity,List<SysDeptEntity> list){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getUpOne().equals(sysDeptEntity.getBrNo())){
                if (!brNoList.contains(list.get(i).getCorpId())) {
                    brNoList.add(list.get(i).getCorpId());
                }
                getChildDept4CorpId(brNoList,list.get(i),list);
            }
        }
    }

    private void getChildDept(List<String> brNoList, SysDeptEntity sysDeptEntity,List<SysDeptEntity> list){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getUpOne().equals(sysDeptEntity.getBrNo())){
                brNoList.add(list.get(i).getBrNo());
                getChildDept(brNoList,list.get(i),list);
            }
        }
    }

    @Override
    public void insert(SysDeptEntity sysDeptEntity) throws ServiceException {
        try {
            sysDeptMapper.insert(sysDeptEntity);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysDeptEntity.getBrNo(),e);
        }
    }

    @Override
    public void update(SysDeptEntity sysDeptEntity) throws ServiceException {
        try {
            sysDeptMapper.updateById(sysDeptEntity);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,"",e);
        }
    }

    @Override
    public SysDeptEntity findById(String brId) throws ServiceException {
        try {
            return sysDeptMapper.selectById(brId);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public void deleteById(String brId) throws ServiceException {
        SysDeptEntity sysDeptEntity = this.findById(brId);
        if(sysDeptEntity != null){
            List<String> brNo = new ArrayList<>();
            brNo.add(sysDeptEntity.getBrNo());
            List<SysUserEntity> sysUserEntityList = sysUserService.findByBrNo(brNo);
            if(sysUserEntityList != null && sysUserEntityList.size() > 0){
                throw new ServiceException(null,null,"已绑定用户，需要解除绑定后才能解除");
            }
        }
        try {
            sysDeptMapper.deleteById(brId);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,"",e);
        }
    }

    @Override
    public void delBatch(Integer id) throws ServiceException {
        Set<Integer> brIds = getIds(id);
        List<String> brNo = new ArrayList<>();
        if(brIds != null && brIds.size()>0){
            List<SysDeptEntity> sysDeptEntityList = sysDeptMapper.selectList(new QueryWrapper<SysDeptEntity>().in("br_id",brIds));
            for(SysDeptEntity sysDeptEntity :sysDeptEntityList){
                brNo.add(sysDeptEntity.getBrNo());
            }
        }
        List<SysUserEntity> sysUserEntityList = sysUserService.findByBrNo(brNo);
        if(sysUserEntityList != null && sysUserEntityList.size() > 0){
            throw new ServiceException(null,null,"已绑定用户，需要解除绑定后才能解除");
        }

        try {
            //查子节点
            sysDeptMapper.deleteBatchIds(brIds);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,"",e);
        }
    }

    private Set<Integer> getIds(Integer parentBrId){
        Set<Integer> list = new TreeSet<>();
        list.add(parentBrId);
        Map<String, Object> m = new HashMap<>();
        m.put("parent_Br_Id", parentBrId);
        List<SysDeptEntity> sysDeptList = sysDeptMapper.selectByMap(m);
        for(SysDeptEntity sysDeptEntity : sysDeptList) {
            Integer id = sysDeptEntity.getBrId();
            list.add(id);
            list.addAll(getIds(id));
        }
        return list;
    }

    //寻找子节点
    private static SysDeptEntity findChildren(SysDeptEntity tree, List<SysDeptEntity> list) {
        for (SysDeptEntity node : list) {
            if (tree.getBrId().equals(node.getParentBrId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<SysDeptEntity>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}

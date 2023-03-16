/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.sys.components.sys.entity.SysParmMoldEntity;
import cn.mftcc.sys.components.sys.mapper.SysParmMoldMapper;
import cn.mftcc.sys.components.sys.service.SysParmMoldService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("sysParmMoldService")
@Transactional(rollbackFor=Exception.class)
public class SysParmMoldServiceImpl implements SysParmMoldService {

    @Autowired
    private SysParmMoldMapper sysParmMoldMapper;
    @Override
    public IPage<SysParmMoldEntity> findByPage(SysParmMoldEntity sysParmMoldEntity) {
        //翻页
        IPage<SysParmMoldEntity> page = new Page<>();
        page.setCurrent(sysParmMoldEntity.getPageNo());
        page.setSize(sysParmMoldEntity.getPageSize());
        QueryWrapper<SysParmMoldEntity> queryWrapper = new QueryWrapper<>();


        return sysParmMoldMapper.selectPage(page,queryWrapper);
    }

    @Override
    public List<SysParmMoldEntity> getList(){
        List<SysParmMoldEntity> list = sysParmMoldMapper.selectList(new QueryWrapper<SysParmMoldEntity>());
        //添加顶级菜单
        SysParmMoldEntity root = new SysParmMoldEntity();
        root.setMoldId("root");
        root.setMoldName("顶级菜单");
        root.setMoldCnt("顶级菜单");
        root.setPid("-root");
        List<SysParmMoldEntity> treeList = findChildren(root, list).getChildren();
        return treeList;
    };

    @Override
    public void insert(SysParmMoldEntity sysParmMoldEntity) {
        sysParmMoldMapper.insert(sysParmMoldEntity);
    }

    @Override
    public void update(SysParmMoldEntity sysParmMoldEntity) {
        sysParmMoldMapper.updateById(sysParmMoldEntity);
    }

    @Override
    public SysParmMoldEntity findById(String moldId) {
        return sysParmMoldMapper.selectById(moldId);
    }

    @Override
    public void deleteById(String moldId) {
        sysParmMoldMapper.deleteById(moldId);
    }

    @Override
    public void delBatch(String id) {
        //查子节点
        Set<String> ids = getIds(id);
        Iterator<String> iter= ids.iterator();
        while(iter.hasNext()) {
            String tid = iter.next();
            sysParmMoldMapper.deleteById(tid);
        }
    }

    private Set<String> getIds(String pid){
        Set<String> list = new TreeSet<>();
        list.add(pid);
        Map<String, Object> m = new HashMap<>();
        m.put("pid", pid);
        List<SysParmMoldEntity> sysParmMoldList = sysParmMoldMapper.selectByMap(m);
        for(SysParmMoldEntity parmMoldEntity : sysParmMoldList) {
            String id = parmMoldEntity.getMoldId();
            list.add(id);
            list.addAll(getIds(id));
        }
        return list;
    }

    //寻找子节点
    private static SysParmMoldEntity findChildren(SysParmMoldEntity tree, List<SysParmMoldEntity> list) {
        for (SysParmMoldEntity node : list) {
            if (node.getPid().equals(tree.getMoldId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<SysParmMoldEntity>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
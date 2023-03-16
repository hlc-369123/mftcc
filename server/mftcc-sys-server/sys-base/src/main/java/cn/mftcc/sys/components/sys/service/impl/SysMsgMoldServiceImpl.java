/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mftcc.sys.components.sys.entity.SysMsgMoldEntity;
import cn.mftcc.sys.components.sys.mapper.SysMsgMoldMapper;
import cn.mftcc.sys.components.sys.service.SysMsgMoldService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("sysMsgMoldService")
@Transactional(rollbackFor=Exception.class)
public class SysMsgMoldServiceImpl implements SysMsgMoldService {

    @Autowired
    private SysMsgMoldMapper sysMsgMoldMapper;
    @Override
    public IPage<SysMsgMoldEntity> findByPage(SysMsgMoldEntity sysMsgMoldEntity) {
        //翻页
        IPage<SysMsgMoldEntity> page = new Page<>();
        page.setCurrent(sysMsgMoldEntity.getPageNo());
        page.setSize(sysMsgMoldEntity.getPageSize());
        QueryWrapper<SysMsgMoldEntity> queryWrapper = new QueryWrapper<>();


        return sysMsgMoldMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysMsgMoldEntity sysMsgMoldEntity) {
        sysMsgMoldMapper.insert(sysMsgMoldEntity);
    }

    @Override
    public void update(SysMsgMoldEntity sysMsgMoldEntity) {
        sysMsgMoldMapper.updateById(sysMsgMoldEntity);
    }

    @Override
    public SysMsgMoldEntity findById(String moldId) {
        return sysMsgMoldMapper.selectById(moldId);
    }

    @Override
    public void deleteById(String moldId) {
            sysMsgMoldMapper.deleteById(moldId);
    }

    @Override
    public List<SysMsgMoldEntity> getList(){
        List<SysMsgMoldEntity> list = sysMsgMoldMapper.selectList(new QueryWrapper<SysMsgMoldEntity>());
        //添加顶级菜单
        SysMsgMoldEntity root = new SysMsgMoldEntity();
        root.setMoldId("root");
        root.setMoldName("顶级菜单");
        root.setMoldCnt("顶级菜单");
        root.setPid("-root");
        List<SysMsgMoldEntity> treeList = findChildren(root, list).getChildren();
        return treeList;
    };

    @Override
    public void delBatch(String id) {
        //查子节点
        Set<String> ids = getIds(id);
        Iterator<String> iter= ids.iterator();
        while(iter.hasNext()) {
            String tid = iter.next();
            sysMsgMoldMapper.deleteById(tid);
        }
    }

    private Set<String> getIds(String pid){
        Set<String> list = new TreeSet<>();
        list.add(pid);
        Map<String, Object> m = new HashMap<>();
        m.put("pid", pid);
        List<SysMsgMoldEntity> sysMsgMoldList = sysMsgMoldMapper.selectByMap(m);
        for(SysMsgMoldEntity sysMsgMoldEntity : sysMsgMoldList) {
            String id = sysMsgMoldEntity.getMoldId();
            list.add(id);
            list.addAll(getIds(id));
        }
        return list;
    }

    //寻找子节点
    private static SysMsgMoldEntity findChildren(SysMsgMoldEntity tree, List<SysMsgMoldEntity> list) {
        for (SysMsgMoldEntity node : list) {
            if (node.getPid().equals(tree.getMoldId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<SysMsgMoldEntity>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
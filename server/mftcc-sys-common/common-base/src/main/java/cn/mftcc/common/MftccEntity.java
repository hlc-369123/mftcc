/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BaseEntity
 * @Description
 * @Author 郭涵晨
 * @Date 2020/2/5 9:39
 */
@Data
public class MftccEntity implements Serializable {

    /**
     * 每页条数
     */
    @TableField(exist = false)
    private int pageSize;
    @TableField(exist = false)
    /**
     * 页数
     */
    private int pageNo;
    @TableField(exist = false)
    /**
     * 动态查询-多列
     */
    private String dynamicQuery;
    @TableField(exist = false)
    /**
     * 自定义筛选-配置
     */
    private String customQuery;
    @TableField(exist = false)
    /**
     * 列表初始化查询条件
     */
    private String initQuery;
    @TableField(exist = false)
    /**
     * 列表表单编号
     */
    private String tableId;
    @TableField(exist = false)
    /**
     * 排序
     */
    private String sort;
    /**
     * 列表多选主键字段
     */
    @TableField(exist = false)
    private String selectKey;
    /**
     * 列表多选选中数据
     */
    @TableField(exist = false)
    private String selectValue;

    /**
     * 列表筛选表达式
     */
    @TableField(exist = false)
    private String queryTab;

}

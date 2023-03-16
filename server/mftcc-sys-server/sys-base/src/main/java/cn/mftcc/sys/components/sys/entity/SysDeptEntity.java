/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("sys_dept")
public class SysDeptEntity  extends MftccEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /****
     * 机构ID
     * ***/
    @TableId(type= IdType.AUTO)
    private Integer  brId;
    /****
     * 法人机构号
     * ***/
    private String  corpId;
    /****
     * 机构级别
     * ***/
    private Integer  brLvl;
    /****
     * 上级机构ID
     * ***/
    private Integer  parentBrId;
    /****
     * 序号
     * ***/
    private Integer  sn;
    /****
     * 机构号
     * ***/
    private String  brNo;
    /****
     * 内部名称
     * ***/
    private String  brName;
    /****
     * 机构全名
     * ***/
    private String  fullBrName;
    /****
     * 显示名称
     * ***/
    private String  displayBrName;
    /****
     * 上一级机构号
     * ***/
    private String  upOne;
    /****
     * 上两级机构号
     * ***/
    private String  upTwo;
    /****
     * 联系电话
     * ***/
    private String  brTel;
    /****
     * 传真
     * ***/
    private String  brFax;
    /****
     * 机构类型
     * ***/
    private String  brTyp;
    /****
     * 机构地址
     * ***/
    private String  brAddr;
    /****
     * 行政区划
     * ***/
    private String  brArea;
    /****
     * 邮政编码
     * ***/
    private String  brPost;
    /****
     * 核心机构号
     * ***/
    private String  coreBrNo;
    /****
     * 信贷虚拟柜员
     * ***/
    private String  coreTel;
    /****
     * 金融机构代码
     * ***/
    private String  brFinCode;
    /****
     * 支付系统行号
     * ***/
    private String  brPayCode;
    /****
     * 机构状态
     * ***/
    private String  brSts;
    /****
     * 是否是法人机构
     * ***/
    private String  isCorp;


    @TableField(exist=false)
    private List<SysDeptEntity> children;

    @TableField(exist = false)
    private String id;

    public String getId() {
        return String.valueOf(brId);
    }

    @TableField(exist = false)
    private String label;

//    public String getLabel() {
//        return brName;
//    }
}

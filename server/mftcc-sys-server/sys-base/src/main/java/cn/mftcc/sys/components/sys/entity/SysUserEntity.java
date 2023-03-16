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

/**
 * 用户
 *
 * @author hgx
 * @email hanguoxing@mftcc.cn
 * @date 2020-03-10 16:30:33
 */
@Data
@TableName("sys_user")
public class SysUserEntity extends MftccEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private int userId; // 用户ID

    private String corpId; // 法人机构号

    private String opNo; // 用户名

    private String password; // 密码

    @TableField(exist = false)
    private String passwordChk; // 确认密码

    private String opName; // 姓名

    private String employeeNo; // 员工号

    private String brNo; // 机构号

    private String roleNo; // 角色号

    @TableField(exist = false)
    private String brName; // 机构名称

    @TableField(exist = false)
    private String roleName; // 角色名称

    private String idTyp; // 证件类型

    private String idNo; // 证件号码

    private String sex; // 性别

    private String tel; // 办公电话

    private String privateTel; // 私人电话

    private String homeTel; // 家庭电话

    private String email; // 办公邮箱

    private String privateEmail; // 私人邮箱

    private String userSts; // 用户状态

    private String  corpNo; //企业账号

    private String birthday; //生日

    @TableField(exist = false)
    private Boolean isSuper; //是否是炒鸡管理猿
}

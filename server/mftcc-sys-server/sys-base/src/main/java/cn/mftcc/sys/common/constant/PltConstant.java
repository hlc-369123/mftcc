/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.common.constant;

public class PltConstant {

    /**
     * 炒鸡管理员opNo
     */
    public static final String SUPER_ADMIN = "^_^super^_^";
    /**
     * 安全审计类型SR-密码长度
     */
    public static final String PR_LENGTH = "1"; //密码长度

    /**
     * 安全审计类型SR-必须包含大写英文字母
     */
    public static final String PR_UPPER_LETTERS = "2"; //必须包含大写英文字母

    /**
     * 安全审计类型SR-必须包含小写英文字母
     */
    public static final String PR_LOWER_LETTERS = "3"; //必须包含小写英文字母

    /**
     * 安全审计类型SR-必须包含数字
     */
    public static final String PR_NUMBER = "4"; //必须包含数字

    /**
     * 安全审计类型SR-必须包含特殊字符
     */
    public static final String PR_SPECIAL_LETTERS = "5"; //必须包含特殊字符

    /**
     * 安全审计类型SR-不能包含用户的账户名
     */
    public static final String PR_NO_OPNO = "6"; //不能包含用户的账户名

    /**
     * 安全审计类型SR-新密码不能与原密码相同
     */
    public static final String PR_NO_SAME = "11"; //新密码不能与原密码相同

    /**
     * 安全审计类型SL-密码最长使用期限
     */
    public static final String SL_MAX_DATE = "7"; //密码最长使用期限

    /**
     * 安全审计类型SL-首次登陆必须修改密码
     */
    public static final String SL_MUST_UPDATE = "10"; //首次登陆必须修改密码

    /**
     * 安全审计类型SF-错误后的容忍次数
     */
    public static final String SF_ERROR_TIMES = "8"; //错误后的容忍次数

    /**
     * 安全审计类型SF-错误后提示剩余次数的次数
     */
    public static final String SF_TIPS_TIMES = "9"; //错误后提示剩余次数的次数
}

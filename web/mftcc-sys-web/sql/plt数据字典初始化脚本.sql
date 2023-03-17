/*
Navicat MySQL Data Transfer

Source Server         : PLTSrv
Source Server Version : 50728
Source Host           : 192.168.10.222:3306
Source Database       : pltsrv

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-10-13 18:26:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_parm_dic
-- ----------------------------
DROP TABLE IF EXISTS `sys_parm_dic`;
CREATE TABLE `sys_parm_dic` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `key_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字典项键值',
  `opt_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '实际值',
  `opt_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '显示值',
  `seqn` decimal(22,0) DEFAULT NULL COMMENT '顺序',
  `sts` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否生效',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `PARM_DIC_INDEX` (`key_name`,`opt_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典项';

-- ----------------------------
-- Records of sys_parm_dic
-- ----------------------------
INSERT INTO `sys_parm_dic` VALUES ('065eb672ff7d8aeb8bd82fef9816feb9', 'PLT_ROLE_TYPE', '2', '内部', '2', '1');
INSERT INTO `sys_parm_dic` VALUES ('111b06a355bf811dec4bb3ea23597f7f', 'PLT_ROLE_TYPE', '1', '外部', '1', '1');
INSERT INTO `sys_parm_dic` VALUES ('163883288158046c4dba6cb78c3c2c95', 'PLT_STS', '0', '停用', '1', '1');
INSERT INTO `sys_parm_dic` VALUES ('1f5a3934d1aae05db0dc84310da5bb73', 'PLT_BR_TYPE', '4', '平行机构', '4', '1');
INSERT INTO `sys_parm_dic` VALUES ('311212d756ac882c57ee28a54ad57cb5', 'PLT_ID_TYPE', '3', '军官证', '4', '1');
INSERT INTO `sys_parm_dic` VALUES ('41ecf65aabe50eec01a2a59ae78d4f0e', 'PLT_STS', '1', '启用', '2', '1');
INSERT INTO `sys_parm_dic` VALUES ('6b78501b765a409aba05e5214a22831c', 'PLT_BR_TYPE', '3', '部门', '3', '1');
INSERT INTO `sys_parm_dic` VALUES ('76e2faba08b52fce098b55e3b3b1b9b1', 'PLT_USER_STS', '1', '启用', '2', '1');
INSERT INTO `sys_parm_dic` VALUES ('786029e4fb9b8c043e03f20c310141da', 'PLT_USER_STS', '2', '注销', '3', '1');
INSERT INTO `sys_parm_dic` VALUES ('7b4a825681595b5818d230ba341b3419', 'PLT_BR_TYPE', '1', '分公司', '1', '1');
INSERT INTO `sys_parm_dic` VALUES ('80d54a3510aab06721c23cd42b033f62', 'PLT_ID_TYPE', '6', '台湾同胞来往内地通行证', '7', '1');
INSERT INTO `sys_parm_dic` VALUES ('8c305288c90e4053f06d61c194b6c996', 'PLT_ID_TYPE', 'B', '社会信用代码证', '12', '1');
INSERT INTO `sys_parm_dic` VALUES ('92a27532817ca3c0d5deb0e802a28964', 'PLT_ID_TYPE', '5', '港澳居民来往内地通行证', '6', '1');
INSERT INTO `sys_parm_dic` VALUES ('a06bf7615c34039d130daab240f0a6a9', 'PLT_SEX', '1', '女', '2', '1');
INSERT INTO `sys_parm_dic` VALUES ('a77f6c7fca73669a5de3dc19679e7272', 'PLT_ID_TYPE', '4', '士兵证', '5', '1');
INSERT INTO `sys_parm_dic` VALUES ('c716c82b2a1268dd1f2ccad3273c66a7', 'PLT_USER_STS', '0', '未生效', '1', '1');
INSERT INTO `sys_parm_dic` VALUES ('d193c74b42638a75b470104a7fbb6c49', 'PLT_ID_TYPE', '0', '身份证', '1', '1');
INSERT INTO `sys_parm_dic` VALUES ('e19397a5fd4f83104ab1e78c9eb80ca4', 'PLT_MSG_TYPE', 'success', '操作成功', '1', '1');
INSERT INTO `sys_parm_dic` VALUES ('e19ec867e1557d8b184fcb79576579b4', 'PLT_BR_TYPE', '2', '办事处', '2', '1');
INSERT INTO `sys_parm_dic` VALUES ('ea443b36742eebc587ae513b3282165f', 'PLT_MSG_TYPE', 'error', '操作失败', '2', '1');
INSERT INTO `sys_parm_dic` VALUES ('fc044287d0cc2a5816ff17ceb02d0f8d', 'PLT_SEX', '0', '男', '1', '1');

-- ----------------------------
-- Table structure for sys_parm_key
-- ----------------------------
DROP TABLE IF EXISTS `sys_parm_key`;
CREATE TABLE `sys_parm_key` (
  `key_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '键值',
  `key_cnt` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `edit` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否可编辑',
  `sts` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否生效',
  `mold_id` varchar(50) DEFAULT NULL COMMENT '服务id',
  PRIMARY KEY (`key_name`) USING BTREE,
  UNIQUE KEY `parm_key_idx1` (`key_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典项键值';

-- ----------------------------
-- Records of sys_parm_key
-- ----------------------------
INSERT INTO `sys_parm_key` VALUES ('PLT_BR_TYPE', '机构类型', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');
INSERT INTO `sys_parm_key` VALUES ('PLT_ID_TYPE', '证件类型', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');
INSERT INTO `sys_parm_key` VALUES ('PLT_MSG_TYPE', '消息模板类型', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');
INSERT INTO `sys_parm_key` VALUES ('PLT_ROLE_TYPE', '角色类型', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');
INSERT INTO `sys_parm_key` VALUES ('PLT_SEX', '性别', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');
INSERT INTO `sys_parm_key` VALUES ('PLT_STS', '启用状态', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');
INSERT INTO `sys_parm_key` VALUES ('PLT_USER_STS', '用户状态', '1', '1', '448c3f258dc1a4042e504218e0d4ffc4');

-- ----------------------------
-- Table structure for sys_parm_mold
-- ----------------------------
DROP TABLE IF EXISTS `sys_parm_mold`;
CREATE TABLE `sys_parm_mold` (
  `mold_id` varchar(50) NOT NULL COMMENT '主键id',
  `pid` varchar(50) DEFAULT NULL COMMENT '父级id',
  `mold_name` varchar(50) DEFAULT NULL COMMENT '类型名称',
  `mold_cnt` varchar(200) DEFAULT NULL COMMENT '类型描述',
  PRIMARY KEY (`mold_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典项类别表';

-- ----------------------------
-- Records of sys_parm_mold
-- ----------------------------
INSERT INTO `sys_parm_mold` VALUES ('448c3f258dc1a4042e504218e0d4ffc4', 'root', 'PLT', '平台服务');

/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */


package cn.mftcc.common;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.sysutils.MsgCacheUtil;
import cn.mftcc.common.utils.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 返回数据
 *
 * @author 
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public R() {
		put("code", 0);
		put("msg", "success");
	}

	public static R ok(){
		return ok(SysConstant.MSG_CONFIG_SUCCESS);
	}

	public static R ok(String msgCode) {
		MsgCacheUtil msgCacheUtil = (MsgCacheUtil) SpringUtil.getBean("msgCacheUtil");
		JSONObject sysMsgConfigEntity = msgCacheUtil.getMsg(msgCode);
		R r = new R();
		if(sysMsgConfigEntity != null){
			r.put("msgLvl", sysMsgConfigEntity.getString("sgLvl"));
			r.put("msgType", sysMsgConfigEntity.getString("msgType"));
			r.put("msg", sysMsgConfigEntity.getString("msgCnt"));
		}else{
			r.put("msgLvl", 1);
			r.put("msgType", "success");
			r.put("msg", msgCode);
		}
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok(String msgCode,Map<String,String> msgCnt) {
		MsgCacheUtil msgCacheUtil = (MsgCacheUtil)SpringUtil.getBean("msgCacheUtil");
		JSONObject sysMsgConfigEntity = msgCacheUtil.getMsg(msgCode);
		R r = new R();
		if(sysMsgConfigEntity != null){
			String msgCntTemplate = sysMsgConfigEntity.getString("msgCnt");
			String regex="\\{(.*?)\\}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(msgCntTemplate);
			while(m.find()){
				if(msgCnt.containsKey(m.group(1))){
					msgCntTemplate = msgCntTemplate.replace(m.group(),msgCnt.get(m.group(1)));
				}
			}
			r.put("msgLvl", sysMsgConfigEntity.getString("sgLvl"));
			r.put("msgType", sysMsgConfigEntity.getString("msgType"));
			r.put("msg", msgCntTemplate);
		}else{
			r.put("msgLvl", 1);
			r.put("msgType", "success");
			r.put("msg", msgCode);
		}
		return r;
	}

	public static R error(){
		return error(SysConstant.MSG_CONFIG_ERROR);
	}

	public static R error(String msgCode) {
		MsgCacheUtil msgCacheUtil = (MsgCacheUtil) SpringUtil.getBean("msgCacheUtil");
		JSONObject sysMsgConfigEntity = msgCacheUtil.getMsg(msgCode);
		R r = new R();
		r.put("code", 500);
		if(sysMsgConfigEntity != null){
			r.put("msgLvl", sysMsgConfigEntity.getString("sgLvl"));
			r.put("msgType", sysMsgConfigEntity.getString("msgType"));
			r.put("msg", sysMsgConfigEntity.getString("msgCnt"));
		}else{
			r.put("msgLvl", 1);
			r.put("msgType", "error");
			r.put("msg", msgCode);
		}
		return r;
	}

	public static R error(String msgCode,Map<String,String> msgCnt) {
		MsgCacheUtil msgCacheUtil = (MsgCacheUtil)SpringUtil.getBean("msgCacheUtil");
		JSONObject sysMsgConfigEntity = msgCacheUtil.getMsg(msgCode);
		R r = new R();
		r.put("code", 500);
		if(sysMsgConfigEntity != null){
			String msgCntTemplate = sysMsgConfigEntity.getString("msgCnt");
			String regex="\\{(.*?)\\}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(msgCntTemplate);
			while(m.find()){
				if(msgCnt.containsKey(m.group(1))){
					msgCntTemplate = msgCntTemplate.replace(m.group(),msgCnt.get(m.group(1)));
				}
			}
			r.put("msgLvl", sysMsgConfigEntity.getString("sgLvl"));
			r.put("msgType", sysMsgConfigEntity.getString("msgType"));
			r.put("msg", msgCntTemplate);
		}else{
			r.put("msgLvl", 1);
			r.put("msgType", "error");
			r.put("msg", msgCode);
		}
		return r;
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	@Override
	public String toString(){
		return JSONObject.toJavaObject((JSON)JSONObject.toJSON(this),JSONObject.class).toJSONString();
	}
}

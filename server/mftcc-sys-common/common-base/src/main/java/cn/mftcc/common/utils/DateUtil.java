/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.utils;

import cn.mftcc.common.logger.MFLogger;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.alibaba.druid.util.JdbcUtils.close;

@Component
public class DateUtil {
	// 日期时间处理工具类

	/**
	 * 日期格式 默认：yyyyMMdd
	 */
	private static final String pattern = "yyyyMMdd";
	/**
	 * 时间格式 默认：HH:mm:ss
	 */
	private static final String timePattern = "HH:mm:ss";


	protected static SqlSessionTemplate sqlSessionTemplate;

	@Resource
	public void  setSqlSessionTemplate  (SqlSessionTemplate sqlSessionTemplate){
		DateUtil.sqlSessionTemplate = sqlSessionTemplate;
	}
	/**
	 * 获取原生SqlSession,需要手动关闭SqlSession
	 * 对应 {@code BaseDaoImpl.closeNativeSqlSession}
	 * @return
	 */
	protected static SqlSession getNativeSqlSession() {
		return SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
				sqlSessionTemplate.getExecutorType(),sqlSessionTemplate.getPersistenceExceptionTranslator());
	}
	/**
	 * 关闭原生SqlSession
	 * 对应 getNativeSqlSession
	 * @return
	 */
	protected static void closeNativeSqlSession(SqlSession sqlSession){
		SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionTemplate.getSqlSessionFactory());
	}

	/**
	 * 取系统营业日期
	 * @return
	 */
	public static String getDate(){
		SqlSession sqlSession = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sysDate = "";
		try {
			String sql = "SELECT * FROM SYS_GLOBAL";
			// 获取SqlSession
			sqlSession = getNativeSqlSession();
			pst = sqlSession.getConnection().prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()){
				sysDate = rs.getString("sys_date");
			}
			return sysDate;
		} catch (SQLException e) {
			MFLogger.error(e.getMessage(),e);
			return sysDate;
		} finally {
			if (pst != null){
				close(pst);
			}
			if (rs != null){
				close(rs);
			}
			if(sqlSession != null){
				// 关闭SqlSession
				closeNativeSqlSession(sqlSession);
			}
		}
	}


	/**
	 * 功能描述：获取系统当前日期---年月日 （格式：yyyymmdd）
	 *
	 * @return String 年月日
	 *
	1.0
	 */
	/*public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = calendar.getTime();
		return format.format(date);
	}*/

	/**
	 * 功能描述：获取系统当前日期---年月日 （格式：yyyymmdd）
	 * 
	 * @return String 时分秒
	 1.0
	 */
	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat(timePattern);
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return format.format(date);
	}

	/**
	 * 功能描述：获取系统时间 格式：yyyymmdd hh:mm:ss
	 * 
	 * @return String
	 * 
	 1.0
	 */
	public static String getDateTime() {
		SimpleDateFormat format = new SimpleDateFormat(pattern + " "
				+ timePattern);
		Calendar cd = new GregorianCalendar(TimeZone.getDefault());
		Date date = cd.getTime();
		return format.format(date);
	}

	/**
	 * 返回两个日期间隔的天数 *
	 *
	 * @param beginDate
	 *            String
	 * @param endDate
	 *            String
	 * @return int
	 */
	public static int getBetweenDays(String beginDate, String endDate) {
		try {
			boolean exchangeFlag = false;
			if (DateUtil.compareDate(beginDate, endDate) == 1) {
				String temp = beginDate;
				beginDate = endDate;
				endDate = temp;
				exchangeFlag = true;
			}
			int sum = 0;
			int beginYear = getCurrentYear(beginDate);
			int beginMonth = getCurrentMonth(beginDate);
			int beginDay = getCurrentDay(beginDate);
			int endYear = getCurrentYear(endDate);
			int endMonth = getCurrentMonth(endDate);
			int endDay = getCurrentDay(endDate);
			String startDateStr = String.valueOf(beginYear) + "-"
					+ bZero(beginMonth) + "-01";

			int sumMonth = (endYear - beginYear + 1) * 12 - (beginMonth)
					- (12 - endMonth);
			for (int i = 0; i < sumMonth; i++) {
				String dateStr = getDateStr(startDateStr, i);
				sum = sum
						+ getMonthDays(getCurrentYear(dateStr),
						getCurrentMonth(dateStr));
			}

			sum = sum - beginDay + endDay;
			if (exchangeFlag) {
				sum = -sum;
			}
			return sum;
		}catch (Exception e){
			return 0;
		}

	}
	/**
	 * 得到当前年 格式是2008-9-25 *
	 *
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getCurrentYear(String dateStr) {
		String[] date = dateStr.split("-");
		return Integer.parseInt(date[0], 10);
	}

	/**
	 * 得到当前月 格式是2008-9-25 *
	 *
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getCurrentMonth(String dateStr) {
		String date[] = dateStr.split("-");
		return Integer.parseInt(date[1], 10);
	}
	/**
	 * 得到当前天数 格式是2008-9-25 *
	 *
	 * @param dateStr
	 *            String
	 * @return int
	 */
	public static int getCurrentDay(String dateStr) {
		String date[] = dateStr.split("-");
		return Integer.parseInt(date[2], 10);
	}
	/**
	 * 返回两位数据字串 *
	 *
	 * @param sz
	 *            int
	 * @return String
	 */
	public static String bZero(int sz) {
		return (sz < 10 ? ("0" + String.valueOf(sz)) : String.valueOf(sz));
	}
	public static int compareDate(String date1, String date2) throws Exception {
		Date date11 = parseTenStrToDate(date1);
		Date date22 = parseTenStrToDate(date2);
		return date11.compareTo(date22);
	}
	public static Date parseTenStrToDate(String str) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse(str);
		return d;
	}
	/**
	 * 返回日期经过若干月后的日期(传入几位日期便返回多少位的格式)
	 *
	 * @param dateStr
	 * @param hkm
	 *            int
	 * @return String
	 */
	public static String getDateStr(String dateStr, int hkm) {
		String st_return = "";
		boolean isEight = true;
		if (dateStr.split("-").length == 3) {
			isEight = false;
		} else {
			dateStr = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6)
					+ "-" + dateStr.substring(6, 8);
		}
		try {
			DateFormat daf_date = DateFormat.getDateInstance(DateFormat.MEDIUM,
					Locale.CHINA);
			daf_date.parse(dateStr);
			Calendar calendar = daf_date.getCalendar();
			calendar.add(Calendar.MONTH, hkm);
			String st_m = "";
			String st_d = "";
			int y = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH) + 1;
			int d = calendar.get(Calendar.DAY_OF_MONTH);
			if (m <= 9) {
				st_m = "0" + m;
			} else {
				st_m = "" + m;
			}
			if (d <= 9) {
				st_d = "0" + d;
			} else {
				st_d = "" + d;
			}
			if (isEight) {
				st_return = y + "" + st_m + "" + st_d;
			} else {
				st_return = y + "-" + st_m + "-" + st_d;
			}
		} catch (ParseException e) {
			MFLogger.error(e.getMessage(),e);
		}
		return st_return;
	}
	/**
	 * 得到月的天数 *
	 *
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */
	public static int getMonthDays(int year, int month) {
		int days = 1;
		boolean isrn = (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) ? true
				: false;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				days = 31;
				break;
			case 2:
				if (isrn)
					days = 29;
				else
					days = 28;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				days = 30;
				break;
			default:
				break;
		}
		return days;
	}

	/**
	 * 将YYYYMMDD转换成YYYY-MM-DD
	 *
	 * @param date
	 * @return
	 */
	public static String parseEightStrToTen(String date) {
		if (null == date || "".equals(date)) {
			return "";
		}
		String dateStr = "";
		try {
			Date d = parseEightStrToDate(date);
			dateStr = parseDateToTenStr(d);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			dateStr = date;
		}
		return dateStr;
	}

	/**
	 * 返回Date *
	 *
	 * @param str
	 *            yyyyMMdd String
	 * @return Date
	 */
	public static Date parseEightStrToDate(String str) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date d = null;
		try {
			d = df.parse(str);
		} catch (ParseException e) {
			MFLogger.error(e.getMessage(),e);
		}
		return d;
	}
	/**
	 * 返回String *
	 *
	 * @param date
	 *            Date
	 * @return String yyyy-MM-dd
	 */
	public static String parseDateToTenStr(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
}

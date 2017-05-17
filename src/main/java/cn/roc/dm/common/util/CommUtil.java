package cn.roc.dm.common.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * 常用辅助方法工具类
 * @author Roc Chen
 * @Date 14:52 2017/5/11
 */
public class CommUtil {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 字符串转换为时间，默认yyyy-MM-dd
	 * @author Roc Chen
	 * @params s:时间字符串
	 * @return java.util.Date
	 * @Date 14:53 2017/5/11
	 */
	public static Date formatDate(String s) {
		Date d = null;
		try {
			d = dateFormat.parse(s);
		} catch (Exception localException) {}
		return d;
	}

	/**
	 * 字符串转换为时间
	 * @author Roc Chen
	 * @params [s：时间字符串, format：转换格式]
	 * @return java.util.Date
	 * @Date 14:54 2017/5/11
	 */
	public static Date formatDate(String s, String format) {
		Date d = null;
		try {
			SimpleDateFormat dFormat = new SimpleDateFormat(format);
			d = dFormat.parse(s);
		} catch (Exception localException) {}
		return d;
	}

	
	
	public static String formatTime(String format, Object v) {
		if(v == null) {
			return null;
		}
		if(v.equals("")) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(v);
	}

	public static String formatLongDate(Object v) {
		if((v == null) || (v.equals(""))) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(v);
	}

	public static String formatShortDate(Object v) {
		if(v == null) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(v);
	}

	

	public static boolean isNotNull(Object obj) {
		if((obj != null) && (!obj.toString().equals(""))) {
			return true;
		}
		return false;
	}


	public static int null2Int(Object s) {
		int v = 0;
		if(s != null) {
			try {
				v = Integer.parseInt(s.toString());
			} catch (Exception localException) {}
		}
		return v;
	}
	
	public static Integer null2Integer(Object s) {
		Integer v = null;
		if(s != null) {
			try {
				v = Integer.parseInt(s.toString());
			} catch (Exception localException) {}
		}
		return v;
	}
	

	public static float null2Float(Object s) {
		float v = 0.0F;
		if(s != null) {
			try {
				v = Float.parseFloat(s.toString());
			} catch (Exception localException) {}
		}
		return v;
	}

	public static double null2Double(Object s) {
		double v = 0.0D;
		if(s != null) {

			try {
				v = Double.parseDouble(null2String(s));
			} catch (Exception localException) {}
		}

		return v;
	}

	public static boolean null2Boolean(Object s) {
		boolean v = false;
		if(s != null) {
			try {
				v = Boolean.parseBoolean(s.toString());
			} catch (Exception localException) {}
		}
		return v;
	}

	public static String null2String(Object s) {
		return s == null ? "" : s.toString().trim();
	}

	public static Long null2Long(Object s) {
		Long v = Long.valueOf(-1L);
		if(s != null) {
			try {
				v = Long.valueOf(Long.parseLong(s.toString()));
			} catch (Exception localException) {}
		}
		return v;
	}

	public static BigDecimal null2BigDecimal(Object s) {
		if(s != null) {
			try {
				if(s instanceof Double || s instanceof Float
					|| s instanceof Integer || s instanceof Long
					|| s instanceof String) {
					return new BigDecimal(s.toString().trim());
				} else {
					return BigDecimal.ZERO;
				}
			} catch (Exception localException) {}
		}
		return BigDecimal.ZERO;
	}

	public static String getTimeInfo(long time) {
		int hour = (int) time / 3600000;
		long balance = time - hour * 1000 * 60 * 60;
		int minute = (int) balance / 60000;
		balance -= minute * 1000 * 60;
		int seconds = (int) balance / 1000;
		String ret = "";
		if(hour > 0) {
			ret = ret + hour + "小时";
		}
		if(minute > 0) {
			ret = ret + minute + "分";
		} else if((minute <= 0) && (seconds > 0)) {
			ret = ret + "零";
		}
		if(seconds > 0) {
			ret = ret + seconds + "秒";
		}
		return ret;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if((ip == null) || (ip.length() == 0)
			|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if((ip == null) || (ip.length() == 0)
			|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if((ip == null) || (ip.length() == 0)
			|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}

		if("0:0:0:0:0:0:0:1".equals(ip)) {
			InetAddress addr = null;

			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			ip = null2String(addr.getHostAddress());
		}
		return ip;
	}


	public static int parseDate(String type, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(type.equals("y")) {
			return cal.get(1);
		}
		if(type.equals("M")) {
			return cal.get(2) + 1;
		}
		if(type.equals("d")) {
			return cal.get(5);
		}
		if(type.equals("H")) {
			return cal.get(11);
		}
		if(type.equals("m")) {
			return cal.get(12);
		}
		if(type.equals("s")) {
			return cal.get(13);
		}

		return 0;
	}

	public static String trimSpaces(String IP) {
		while (IP.startsWith(" ")) {
			IP = IP.substring(1, IP.length()).trim();
		}
		while (IP.endsWith(" ")) {
			IP = IP.substring(0, IP.length() - 1).trim();
		}
		return IP;
	}

	public static boolean isIp(String IP) {
		boolean b = false;
		IP = trimSpaces(IP);
		if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			String[] s = IP.split("\\.");
			if((Integer.parseInt(s[0]) < 255)
				&& (Integer.parseInt(s[1]) < 255)
				&& (Integer.parseInt(s[2]) < 255)
				&& (Integer.parseInt(s[3]) < 255)) {
				b = true;
			}
		}
		return b;
	}

	public static String generic_domain(HttpServletRequest request) {
		String system_domain = "localhost";
		String serverName = request.getServerName();
		if(isIp(serverName)) {
			system_domain = serverName;
		} else if(serverName.indexOf(".") == serverName.lastIndexOf(".")) {
			system_domain = serverName;
		} else {
			system_domain = serverName.substring(serverName.indexOf(".") + 1);
		}
		return system_domain;
	}



	/**
	 * 获取链接的后缀名
	 * 
	 * @return
	 */

	final static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

	public static String parseSuffix(String url) {

		Matcher matcher = pattern.matcher(url);

		String[] spUrl = url.toString().split("/");
		int len = spUrl.length;
		String endUrl = spUrl[len - 1];

		if(matcher.find()) {
			String[] spEndUrl = endUrl.split("\\?");
			String filename = spEndUrl[0];
			String[] nodes = filename.split("\\.");
			int length = nodes.length;
			return nodes[length - 1];
		} else {
			String filename = endUrl;
			String[] nodes = filename.split("\\.");
			int length = nodes.length;
			return nodes[length - 1];
		}
	}

	public static String getURL(HttpServletRequest request) {
		String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
		String url = request.getScheme() + "://" + request.getServerName();
		String port = String.valueOf(null2Int(Integer.valueOf(request.getServerPort())));
		if (null2Int(Integer.valueOf(request.getServerPort())) != 80) {
			url = url + ":" + port + contextPath;
		} else {
			url = url + contextPath;
		}
		return url;
	}
	
	public static boolean isPwdWithCharAndNum(String password, int minLength, int maxLength) {
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{"+ String.valueOf(minLength) +","+ String.valueOf(maxLength) +"}$";					
		return password.matches(regex);
	}
	
	/**
	 * 
	 * 
	 *
	 * @author Jack.Huang
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if ("".equals(CommUtil.nullToString(obj))) {
			return true;
		}
		return false;
	}
	
	public static String nullToString(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}
	
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if ("".equals(str)) {
			return true;
		}
		return false;
	}
	
	public static String nullToString(String str) {
		return (str == null) ? "" : str.trim();
	}
	
	/**
	 * 获取商品折扣
	 * @param price
	 * @param oldPrice
	 * @return
	 */
	public static String getDiscount(BigDecimal price, BigDecimal oldPrice) {		
		String discount = "";
		if (!StringUtils.isEmpty(price) && !StringUtils.isEmpty(oldPrice)) {
			BigDecimal dc = price.divide(oldPrice,3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP);			
			// 折扣大于等于10.0都返回10.0
			if (dc.compareTo(new BigDecimal(10.0)) >= 0) {
				discount = "10.0";
			}else{
				discount = dc.toString();
			}
		}
		return discount;

	}
	
	/**
	 * 获取格式化后的金额
	 * @param Amount
	 * @return
	 */
	public static String formatAmount(String Amount) {		
		String formatAmount = "";
		if (!StringUtils.isEmpty(Amount)) {
			try {
				NumberFormat nf = new DecimalFormat("#.##");
				BigDecimal bd = new BigDecimal(Amount);
				formatAmount = nf.format(bd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return formatAmount;
	}
	
	/**
	 * 两个时间相差多少个小时
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getSecondsBetweenDate(String start,String end){
		long seconds = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = df.parse(start);
			Date endDate = df.parse(end);
			seconds = (endDate.getTime()-startDate.getTime())/1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seconds;
	}
	
	/**
	 * 某个时间与系统时间相差多少秒
	 * @param time
	 * @return
	 */
	public static String getSecondsToCurrentTime(String time) {
		long seconds = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (null != time && time.length() != 0) {
				Date startDate = df.parse(time);
				seconds = (startDate.getTime() - System.currentTimeMillis()) / 1000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seconds + "";
	}
	
	public static void main(String[] args){
		String start = "2016-10-08 08:25:30";
		String end = "2016-10-08 08:53:23";
		System.out.println(getSecondsBetweenDate(start, end)/60);
	}
	
	/**
	 * 
	  * @Title: checkEmoji
	  * @Description: 校验参数长度并判断是否包含4个字节的表情符号
	  * @param @param parameter
	  * @param @param length
	  * @param @return
	  * @return boolean
	  * @throws 
	  * @author leiqiang
	  * @date 2016年4月26日
	 */
	public static boolean checkEmoji(String parameter) {
		
		try {
//			if (parameter.length() > length) {
//				return false;
//			}
			
			//去掉汉字，避免误处理
			parameter = parameter.replaceAll("[\\u4e00-\\u9fa5]", "");
			
			if (!StringUtils.isEmpty(parameter)) {
				byte[] parByte = parameter.getBytes();
				for (int i = 0; i < parByte.length; i++) {
					if ((parByte[i] & 0xF8) == 0xF0) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}

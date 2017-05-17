package cn.roc.dm.common.util;

import java.math.BigDecimal;
import cn.roc.wt.common.exception.ParamException;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 请求参数验证工具类
 * @author Roc Chen
 * @Date 14:41 2017/5/11
 */
public class ParamCheckUtil {
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param nullErrorCode
	 * @return
	 * @throws ParamException
	 */
	public static String checkParamIsNull(String param,String value,int nullErrorCode) throws ParamException{
		if(StringUtil.isEmpty(value))
			throw new ParamException(nullErrorCode,MsgConstent.getMsg(nullErrorCode, param));
		return value.trim();
	}
	
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param errorCode
	 * @param minValue
	 * @return
	 * @throws ParamException
	 */
	public static int checkParamIsInt(String param,String value,int errorCode,int minValue) throws ParamException{
		if(StringUtil.isEmpty(value))
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		int i = 0;
		try{
			i = Integer.parseInt(value.trim());
		}catch(NumberFormatException e){
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param),e);
		}
		if(i < minValue)
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		return i;
	}
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param errorCode
	 * @return
	 * @throws ParamException
	 */
	public static int checkParamIsInt(String param,String value,int errorCode) throws ParamException{

		if(StringUtil.isEmpty(value))
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		int i = 0;
		try{
			i = Integer.parseInt(value.trim());
		}catch(NumberFormatException e){
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param),e);
		}
		return i;
	}
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param errorCode
	 * @return
	 * @throws ParamException
	 */
	public static long checkParamIsLong(String param,String value,int errorCode) throws ParamException{

		if(StringUtil.isEmpty(value))
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		long i = 0;
		try{
			i = Long.parseLong(value.trim());
		}catch(NumberFormatException e){
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param),e);
		}
		return i;
	}

	/**
	 *
	 * @param param
	 * @param values
	 * @param errorCode
	 * @return
	 * @throws ParamException
	 */
	public static int checkParamValueRange(String param,int value,int errorCode,int... values) throws ParamException{

		if(ArrayUtils.isEmpty(values)){
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		}
		boolean right=false;
		for (int allowValue : values) {
			if (value == allowValue) {
				right = true;
				break;
			}
		}
		if (!right) {
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		}
		return value;
	}
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param errorCode
	 * @param minValue
	 * @return
	 * @throws ParamException
	 */
	public static BigDecimal checkParamIsBigDecimal(String param,String value,int errorCode,int minValue) throws ParamException{

		if(StringUtil.isEmpty(value))
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		BigDecimal i = null;
		try{
			i = CommUtil.null2BigDecimal(value);
		}catch(NumberFormatException e){
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param),e);
		}
		if(i.compareTo(new BigDecimal(0.00)) <= minValue)
			throw new ParamException(errorCode,MsgConstent.getMsg(errorCode, param));
		return i;
	}
	
	
	/**
	 * 
	 * @param param
	 * @param value
	 * @param errorCode
	 * @return
	 * @throws ParamException
	 */
	public static double checkParamIsDouble(String param, String value, int errorCode) throws ParamException {
		if(StringUtil.isEmpty(value))
			throw new ParamException(errorCode, MsgConstent.getMsg(errorCode, param));
		double d = 0.0;
		try{
			d = Double.parseDouble(value.trim());
		}catch(NumberFormatException e){
			throw new ParamException(errorCode, MsgConstent.getMsg(errorCode, param), e);
		}
		return d;
	}
	
}

package cn.roc.dm.common.util;

/**
 * 系统返回信息枚举类
 * @author Roc Chen
 * @Date 14:56 2017/5/11
 */
public enum MsgConstent {

	success("成功", 0),

	SYSTEM_EXCEPTION("系统错误", 1000001),

	/*参数校验类错误*/
	PARAMETER_NULL("参数不可为空", 3000001)

	;

	private String name;
	private int code;
	private String title;

	//构造方法
	private MsgConstent(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	//构造方法
	private MsgConstent(Integer code) {
		this.code = code;
	}

	//构造方法
	private MsgConstent(String name, Integer code, String title) {
		this.name = name;
		this.code = code;
		this.title = title;
	}

	//普通方法
	public static String getCodeName(Integer code) {
		for (MsgConstent c : MsgConstent.values()) {
			if (c.getCode() == code) {
				return c.name;
			}
		}
		return null;
	} // get set 方法

	//普通方法
	public static String getMsg(Integer code, String title) {
		for (MsgConstent c : MsgConstent.values()) {
			if (c.getCode() == code) {
				return title + c.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

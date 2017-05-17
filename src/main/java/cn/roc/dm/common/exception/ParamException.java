package cn.roc.dm.common.exception;

/**
 * 自定义异常
 * @see ParamException
 * @author Roc Chen
 * @Date 14:25 2017/5/11
 */
public class ParamException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
     * 错误码
     */
	private int errorCode;
	/**
     * 错误内容
     */
	private String errorContent;
    
    /**
     * 构造方法
     * 
     * @param errorCode 错误码
     * @param errorContent 错误内容
     */
    public ParamException(int errorCode, String errorContent)
    {
        this.errorCode = errorCode;
        this.errorContent = errorContent;
    }
    
    /**
     * 构造方法
     * 
     * @param throwable 错误基类
     */
    public ParamException(int errorCode, String errorContent,Throwable throwable)
    {
        super(throwable);
        this.errorCode = errorCode;
        this.errorContent = errorContent;
    }
    
    /**
     * 
     * Description:获取错误码 <br>
     * 
     * @return 错误码
     * @see
     */
    public int getErrorCode()
    {
        return errorCode;
    }
    
    /**
     * 
     * Description:设置错误码 <br>
     * 
     * @param errorNo 错误码
     * @see
     */
    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }
    
    /**
     * 
     * Description:获取错误内容 <br>
     * 
     * @return 错误内容
     * @see
     */
    public String getErrorContent()
    {
        return errorContent;
    }
    
    /**
     * 
     * Description:设置错误内容 <br>
     *
     * @param errorContent 错误内容
     * @see
     */
    public void setErrorContent(String errorContent)
    {
        this.errorContent = errorContent;
    }
    
    /**
     * 
     * Description:重写toString方法 <br>
     * 
     * @return String
     * @see
     */
    public String toString()
    {
        return errorContent;
    }
}

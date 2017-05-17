package cn.roc.dm.common.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.roc.wt.common.log.LoggerFactoryUtil;
import cn.roc.wt.common.util.CommUtil;
import cn.roc.wt.common.util.Constants;
import cn.roc.wt.common.util.StringUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * 拦截器打印接口处理总时间
 *
 */
public class LogInterceptor extends HandlerInterceptorAdapter {
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LoggerFactoryUtil.access("Request|" + getParamStr(request));
		//设置请求开始时间，用于计算接口总耗时
		request.setAttribute(Constants.REQUEST_START_TIME, System.currentTimeMillis());
		return true;
	}
	
	@Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		long totalTime = 0L;
		String log = null;
		try{
	        if (request.getAttribute(Constants.REQUEST_START_TIME) != null) {
	        	totalTime = System.currentTimeMillis() - Long.valueOf(request.getAttribute(Constants.REQUEST_START_TIME).toString());
	        }
	        // 打印接口请求时间
	        log = "request_url:"+CommUtil.getURL(request)+request.getServletPath()+"|ip:"+ CommUtil.getIpAddr(request)+"|t_id="+getTidParam(request);
	        LoggerFactoryUtil.access("Response|totalTime="+totalTime+"ms|"+log);
	       if (totalTime > 300) {
	            // 超过300毫秒的打印出接口
	        	LoggerFactoryUtil.info("Response totalTime too long!, totalTime=" + totalTime + "ms|" + log);
	        }
		}catch(Exception e){
			// 打印接口请求时间
			LoggerFactoryUtil.error("Response|totalTime=" + totalTime +"ms|"+log,e);
		}finally{
			log = null;
		}
    }
	
	@SuppressWarnings("unchecked")
	private String getParamStr(HttpServletRequest request) {
        String queryString = "";
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (value.length() > 50) {
                    // 如果参数太长了，搞短一点再打印
//                    if (!"data".equals(key)) {// 如果不是data字段，要进行截取
//                        value = value.substring(0, 50) + "......";
//                    }
                    queryString += key + "=" + value + "&";
                } else {
                    queryString += key + "=" + value + "&";
                }
            }
        }
        String token = request.getHeader("token");
        if (StringUtil.isEmpty(token)) {
            token = request.getParameter("token");
        }
        queryString += "&token=" + token;
        String userAgent = StringUtil.trim(request.getHeader("User-Agent"));
        return "url=" + CommUtil.getURL(request)+request.getServletPath() + "?" + queryString + "&userAgent=" + userAgent + "&ip=" + CommUtil.getIpAddr(request);
    }
	
	private String getTidParam(HttpServletRequest request){
		return request.getParameter(Constants.RETURN_TID);
	}
}

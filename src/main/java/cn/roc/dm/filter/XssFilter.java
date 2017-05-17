package cn.roc.dm.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * XSS漏洞过滤器
 * @author Roc Chen
 * @Date 14:02 2017/5/11
 */
public class XssFilter implements Filter {

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Map<String, String[]> paramMap = request.getParameterMap();
		Set<String> keySet = paramMap.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String[] str = paramMap.get(key);
			for (int i = 0; i < str.length; i++) {
				str[i] = FilterUtil.filterXss(str[i]);
			}
		}
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		filterChain.doFilter(xssRequest, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// do nothing

	}
}

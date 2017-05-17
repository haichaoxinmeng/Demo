package cn.roc.dm.filter;

/**
 * XSS漏洞过滤器
 * @author Roc Chen
 * @Date 14:02 2017/5/11
 */
public class FilterUtil {


    public static String filterXss(String content){
    	content = content.replaceAll("<", "&lt;")
		  .replaceAll(">", "&gt;")
		  .replaceAll("'", "&#39;")
		  .replaceAll("&", "&amp;");
		//参数经过防止XXS攻击后将&amp;替换成&amp;amp;的情况
    	content = content.replaceAll("&amp;amp;", "&amp;")
		  .replaceAll("&amp;gt;", "&gt;")
		  .replaceAll("&amp;lt;", "&lt;")
		  .replaceAll("&amp;apos;", "&apos;")
		  .replaceAll("&amp;quot;", "&quot;")
		  .replaceAll("&amp;nbsp;", "&nbsp;")
		  .replaceAll("&amp;#61;", "&#61;")
		  .replaceAll("&amp;#96;", "&#96;")
		  .replaceAll("&amp;#39;", "&#39;");
    	return content;
    }

    public static String deFilterXss(String content) {
        content = content .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&#39;", "'")
                .replaceAll("&#39;", "&amp;#39;")
                .replaceAll("&#96;", "&amp;#96;")
                .replaceAll("&#61;","&amp;#61;")
                .replaceAll( "&nbsp;","&amp;nbsp;")
                .replaceAll( "&quot;","&amp;quot;")
                .replaceAll("&apos;","&amp;apos;")
                .replaceAll("&lt;","&amp;lt;")
                .replaceAll("&gt;","&amp;gt;")
                .replaceAll("&amp;", "&amp;amp;")
                .replaceAll("&amp;", "&");
        //参数经过防止XXS攻击后将&amp;替换成&amp;amp;的情况
        return content;
    }

}

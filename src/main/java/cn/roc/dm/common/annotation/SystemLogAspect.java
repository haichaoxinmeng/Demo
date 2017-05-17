package cn.roc.dm.common.annotation;

import java.lang.reflect.Method;

import cn.roc.wt.common.log.LoggerFactoryUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;



@Aspect
@Component
public class SystemLogAspect {

	
	//Service层切点    
    @Pointcut("@annotation(cn.roc.wt.common.annotation.SystemServiceLog)")
    public  void serviceAspect() {}
    
    //Controller层切点    
    @Pointcut("@annotation(cn.roc.wt.common.annotation.SystemControllerLog)")
     public  void controllerAspect() {}
    
    
    
    /**
     * controller 层 返回结果日志打印
     * @param joinPoint
     * @param retVal
     */
    @AfterReturning(value="controllerAspect()",argNames = "retVal",returning = "retVal")
    public void doAfter(JoinPoint joinPoint,Object retVal) {
    	
       try {
    	    if(retVal instanceof String){
    	    	 LoggerFactoryUtil.access(getControllerMethodDescription(joinPoint)+" result of returns : "+retVal);
    	    }else{
    	    	LoggerFactoryUtil.access(getControllerMethodDescription(joinPoint)+" result of returns : "+JSON.toJSONString(retVal));
    	    }
	   } catch (Exception e) {
		   LoggerFactoryUtil.error("com.ea.bic.common.annotation.SystemLogAspect.controllerAspect.affterReturning 异常", e);
	   }
    	
    }
    
    
    
    /**  
     * 获取注解中对方法的描述信息 用于service层注解  
     *  
     * @param joinPoint 切点  
     * @return 方法描述  
     * @throws Exception  
     */    
     @SuppressWarnings("rawtypes")
	public  static String getServiceMthodDescription(JoinPoint joinPoint)    
             throws Exception {    
        String targetName = joinPoint.getTarget().getClass().getName();    
        String methodName = joinPoint.getSignature().getName();    
        Object[] arguments = joinPoint.getArgs();    
        Class targetClass = Class.forName(targetName);    
        Method[] methods = targetClass.getMethods();    
        String description = "";    
         for (Method method : methods) {    
             if (method.getName().equals(methodName)) {    
                Class[] clazzs = method.getParameterTypes();    
                 if (clazzs.length == arguments.length) {    
                    description =targetName+"."+methodName+": "+method.getAnnotation(SystemServiceLog. class).description();    
                     break;
                }    
            }    
        }    
         return description;    
     }    
    
    /**  
     * 获取注解中对方法的描述信息 用于Controller层注解  
     *  
     * @param joinPoint 切点  
     * @return 方法描述  
     * @throws Exception  
     */    
     @SuppressWarnings("rawtypes")
	public  static String getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();    
        String methodName = joinPoint.getSignature().getName();    
        Object[] arguments = joinPoint.getArgs();    
        Class targetClass = Class.forName(targetName);    
        Method[] methods = targetClass.getMethods();    
        String description = "";    
         for (Method method : methods) {    
             if (method.getName().equals(methodName)) {    
                Class[] clazzs = method.getParameterTypes();    
                 if (clazzs.length == arguments.length) {
                    description =targetName+"."+methodName+": "+method.getAnnotation(SystemControllerLog. class).description();    
                     break;
                }
            }
        }
         return description;    
    }    
}

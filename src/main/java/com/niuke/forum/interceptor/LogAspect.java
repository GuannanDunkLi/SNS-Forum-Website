package com.niuke.forum.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义一个切入点.
     * 解释下：
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 定义在web包或者子包
     * ~ 第三个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.niuke.forum.controller..*.*(..))")
    public void logPointcut() {
    }

    @org.aspectj.lang.annotation.Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            // 获取目标类名称
            String clazzName = joinPoint.getTarget().getClass().getName();
            // 获取目标类方法名称
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            logger.info("+++++around " + clazzName + "\t" + methodName + "\tUse time : " + (end - start) + " ms!");
            return result;
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            logger.info("+++++around " + joinPoint.getSignature().getName() + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
            throw e;
        }
    }
}

package com.epam.esm.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger();

    @Pointcut("execution(* com.epam.esm.service.*.*(..))")
    public void performance(){}

    @Before("performance()")
    public void beforeServiceMethodInvocation(JoinPoint joinPoint){
        logger.log(Level.INFO, "Invocation  of method " + joinPoint.getSignature());
    }

    @After("performance()")
    public void afterServiceMethodInvocation(JoinPoint joinPoint){
        logger.log(Level.INFO, "Finish - " + joinPoint.getTarget());
    }
}

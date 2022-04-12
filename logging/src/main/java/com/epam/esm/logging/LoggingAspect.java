package com.epam.esm.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * The type Logging aspect is annotated as an aspect class.
 * @author Anna Merkul
 */
@Aspect
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Performance. Contains a pointcut which shows for which classes and methods to use.
     */
    @Pointcut("execution(* com.epam.esm.service.*.*(..))")
    public void performance(){}

    /**
     * Before service method invocation.
     *
     * @param joinPoint the join point
     */
    @Before("performance()")
    public void beforeServiceMethodInvocation(JoinPoint joinPoint){
        logger.log(Level.INFO, "Invocation  of method " + joinPoint.getSignature());
    }

    /**
     * After service method invocation.
     *
     * @param joinPoint the join point
     */
    @After("performance()")
    public void afterServiceMethodInvocation(JoinPoint joinPoint){
        logger.log(Level.INFO, "Finish - " + joinPoint.getTarget());
    }
}

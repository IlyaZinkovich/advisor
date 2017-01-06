package com.routes.advisor.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("within(com.routes.advisor.*.*)")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        LOGGER.debug("Method " + pjp.getSignature() + "started execution");
        Object output = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        LOGGER.debug("Method " + pjp.getSignature() + " execution time: " + elapsedTime + " milliseconds.");
        return output;
    }
}

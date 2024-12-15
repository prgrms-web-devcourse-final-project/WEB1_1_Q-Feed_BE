package com.wsws.moduleinfra.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 락 이름
     */
    String key();

    /**
     * 락 시간 단위
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 락을 기다리는 시간
     */
    long waitTime() default 500L;

    /**
     * 락 임대 시간
     */
    long leaseTime() default 300L;
}

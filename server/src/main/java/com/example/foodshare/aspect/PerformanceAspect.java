package com.example.foodshare.aspect;import org.aspectj.lang.ProceedingJoinPoint;import org.aspectj.lang.annotation.Around;import org.aspectj.lang.annotation.Aspect;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.stereotype.Component;@Aspect@Componentpublic class PerformanceAspect {	private static final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);	@Around("execution(* com.example.foodshare..*(..))")	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {		long startTime = System.currentTimeMillis();		try {			return joinPoint.proceed(); // 메소드 실행		} finally {			long executionTime = System.currentTimeMillis() - startTime;			double executionTimeInSeconds = executionTime / 1000.0;			logger.info("{} executed in {} seconds", joinPoint.getSignature(),				String.format("%.2f", executionTimeInSeconds));		}	}}
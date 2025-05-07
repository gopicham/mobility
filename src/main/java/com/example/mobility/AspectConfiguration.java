/**
 * 
 */
package com.example.mobility;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 */

@Aspect
@Component
public class AspectConfiguration {

	private Logger logger;

	@Around("execution(* com.example.mobility.controller.*.*(..))")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		logger.info("method started executing in" + "::" + joinPoint.getSignature().getName() + "()::method::{}",
				Arrays.toString(joinPoint.getArgs()));

		logger.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

		Object result = joinPoint.proceed();

		long end = System.currentTimeMillis();

		logger.info("Time take to execute::" + joinPoint.getSignature().getName() + "()::method::{}", (end - start));
		return result;
	}

	@Before("execution(* com.example.mobility.controller.*.*(..))")
	public void beforeMethodExecution(JoinPoint joinPoint) {
		logger.info("method started in beforeMethodExecution() with param::{}", joinPoint.getSignature());

	}

	@After("execution(* com.example.mobility.controller.*.*(..))")
	public void afterMethodExecution(JoinPoint joinPoint) {
		logger.info("method started in afterMethodExecution() with param::{}", joinPoint.getSignature());
	}

	@AfterThrowing("execution(* com.example.mobility.controller.*.*(..))")
	public void afterCompletionMethodExecution(JoinPoint joinPoint, Exception e) {
		logger.info("method started in afterMethodExecution() with param::{}", e);
	}

	@Around("execution(* com.example.mobility.controller.MobilityController.sayHello(..))")
	public Object modifyReturnValue(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();

		if (result instanceof String) {
			String user = (String) result;
			result = user + "_aop_modified";
		}
		logger.info("method started in modifyReturnValue() with param::{}", result);
		return result;
	}

	/**
	 * Pointcut that matches all repositories, services and Web REST endpoints.
	 */
	@Pointcut("within(@org.springframework.stereotype.Repository *)"
			+ " || within(@org.springframework.stereotype.Service *)"
			+ " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	/**
	 * Pointcut that matches all Spring beans in the application's main packages.
	 */
	@Pointcut("within(net.guides.springboot2.springboot2jpacrudexample..*)"
			+ " || within(net.guides.springboot2.springboot2jpacrudexample.service..*)"
			+ " || within(net.guides.springboot2.springboot2jpacrudexample.controller..*)")
	public void applicationPackagePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	/**
	 * Advice that logs methods throwing exceptions.
	 *
	 * @param joinPoint join point for advice
	 * @param e         exception
	 */
	@AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		logger.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	/**
	 * Advice that logs when a method is entered and exited.
	 *
	 * @param joinPoint join point for advice
	 * @return result
	 * @throws Throwable throws IllegalArgumentException
	 */
	@Around("applicationPackagePointcut() && springBeanPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		if (logger.isDebugEnabled()) {
			logger.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}
		try {
			Object result = joinPoint.proceed();
			if (logger.isDebugEnabled()) {
				logger.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
						joinPoint.getSignature().getName(), result);
			}
			return result;
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		}
	}
}

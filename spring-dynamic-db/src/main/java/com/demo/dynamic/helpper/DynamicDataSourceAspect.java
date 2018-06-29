package com.demo.dynamic.helpper;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class DynamicDataSourceAspect {
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Before("@annotation(com.demo.dynamic.helpper.DataSource)")
	public void before(JoinPoint point) {
		operatePointCut(point, false);
	}

	@After("@annotation(com.demo.dynamic.helpper.DataSource)")
	public void after(JoinPoint point) {
		operatePointCut(point, true);
	}

	private void operatePointCut(JoinPoint point, boolean isReset) {
		Class<?> target = point.getTarget().getClass();
		MethodSignature signature = (MethodSignature) point.getSignature();
		// 默认使用目标类型的注解，如果没有则使用其实现接口的注解类
		for (Class<?> cls : target.getInterfaces()) {
			operateDataSource(cls, signature.getMethod(), isReset);
		}
		operateDataSource(target, signature.getMethod(), isReset);
	}

	private void operateDataSource(Class<?> cls, Method method, boolean isReset) {
		try {
			Class<?>[] types = method.getParameterTypes();
			if (!isReset) {
				if (cls.isAnnotationPresent(DataSource.class)) {
					DataSource source = cls.getAnnotation(DataSource.class);
					DataSourceContextHolder.setDataSourceType(source.value());
					System.out.println("routeKeyClz:" + source.value());
				}
			}

			// 方法注解可以覆盖类注解
			Method m = cls.getMethod(method.getName(), types);
			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource source = m.getAnnotation(DataSource.class);
				if (isReset) {
					DataSourceContextHolder.clearDataSourceType();
				} else {
					DataSourceContextHolder.setDataSourceType(source.value());
				}
				System.out.println("routeKeyMethod:" + source.value());
			}

		} catch (Exception e) {
			logger.error(cls + ":" + e.getMessage(), e);
		}
	}
}
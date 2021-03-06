package application;

import java.lang.reflect.Method;

import org.springframework.aop.framework.ProxyFactory;

public class TxMethodProxy {
	private TxMethodProxy(){}

	public static TxMethodInvocation getMethodInvocation(Object target, Method method, Object[] args)
			throws Throwable {
		 ProxyFactory proxyFactory = new ProxyFactory(target);
		 TxMethodInvocation txMethodInvocation = new TxMethodInvocation(proxyFactory.getProxy(), target, 
				 method, args, proxyFactory.getTargetClass(), 
				 proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(method, proxyFactory.getTargetClass()));
		return txMethodInvocation;
	}


}

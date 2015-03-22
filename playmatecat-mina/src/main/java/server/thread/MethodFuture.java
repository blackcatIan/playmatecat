package server.thread;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.concurrent.Callable;

import net.minidev.json.JSONValue;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;

import application.ApplicationContextHolder;

import com.playmatecat.mina.NioTransferAdapter;

/**
 * 调用spring的bean的方法的真正线程体,由于继承callable因此可以阻塞返回结果(外部再套个线程调用get结果以确保整个程序非阻塞)
 * @author blackcat
 *
 */
public class MethodFuture implements Callable<String>{
	
	/**nio server session**/
	private IoSession session; 
	/**nio 服务端收到的数据**/
	private Object message;
	
	public MethodFuture(IoSession session, Object message) {
		this.session = session;
		this.message = message;
	}
	
	public String call() throws Exception {
		
		NioTransferAdapter nta = (NioTransferAdapter) message;
		
		//请求的服务名
		String restServiceName = nta.getRestServiceName();
		
		String ctpName = "get From db by restServiceName";
		String ctpMethodName = "get From db by restServiceName";
		
		ctpName = "userCpt";
		ctpMethodName = "testCall";
		
		//获得组件名
		Object reflectCpt = ApplicationContextHolder.getApplicationContext().getBean(ctpName);
		//nta.getClazz获得DTO的类型，作为反射调用函数的入参类型
		String result = StringUtils.EMPTY;
		if(nta.getClazz() != null) {
			//有参调用
			Method method = reflectCpt.getClass().getMethod(ctpMethodName,nta.getClazz());
			result = (String) method.invoke(reflectCpt,JSONValue.parse(nta.getJSONdata(),nta.getClazz()));
		} else {
			//无参调用
			Method method = reflectCpt.getClass().getMethod(ctpMethodName);
			result = (String) method.invoke(reflectCpt);
		}
		
		return result;
	}

}

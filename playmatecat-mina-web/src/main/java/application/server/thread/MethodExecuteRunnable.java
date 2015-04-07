package application.server.thread;

import java.lang.reflect.Method;

import net.minidev.json.JSONValue;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import application.ApplicationContextHolder;

import com.playmatecat.mina.NioTransferAdapter;


public class MethodExecuteRunnable extends Thread {
	
	private final static Logger logger = Logger.getLogger(MethodExecuteRunnable.class);
	
	/**nio server session**/
	private IoSession session; 
	/**nio 服务端收到的数据**/
	private Object message;
	/**调用bean方法最大执行时间**/
	private final static int MAX_METHOD_RUNNING_TIME = 1;
	
	public MethodExecuteRunnable(IoSession session, Object message) {
		this.session = session;
		this.message = message;
	}

	@Override
	public void run() {
		NioTransferAdapter nta = (NioTransferAdapter) message;
		
		//请求唯一标码
		String GUID = nta.getGUID();
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
		
		try {
			if(nta.getClazz() != null) {
				//有参调用
				//反射方法用了很多时间！！所以用缓存
				//尝试从缓存中取得需要执行方法,找不到再用反射
				String keyName = ctpMethodName + nta.getClazz().getName();
				Method method = MethodCache.methodMap.get(keyName);
				if(method == null) {
					method = reflectCpt.getClass().getMethod(ctpMethodName,nta.getClazz());
					MethodCache.methodMap.put(keyName, method);
				}
				result = (String) method.invoke(reflectCpt,JSONValue.parse(nta.getJSONdata(),nta.getClazz()));
				//result = "abc";
			} else {
				//无参调用
				Method method = reflectCpt.getClass().getMethod(ctpMethodName);
				result = (String) method.invoke(reflectCpt);
			}
		} catch (Exception e) {
			logger.error("反射方法调用错误", e);
		}
		
		
		//返回数据，并且设定相同的唯一标码来保证客户端识别是哪次请求
		NioTransferAdapter rtnNta = new NioTransferAdapter(result);
		rtnNta.setGUID(GUID);
		session.write(rtnNta);

		MethodExecutePool.runningThreadCount--;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
////		FutureTask<String> futureTask = new FutureTask<String>(new MethodFuture(session, message));
////		
////		Thread thread = new Thread(futureTask);
////		thread.start();
////		
////		String result;
////		try {
////			//最大数据请求时间1分钟
////			result = futureTask.get(MAX_METHOD_RUNNING_TIME, TimeUnit.SECONDS);
////		} catch (Exception e) {
////			//处理调用方法超时
//////			logger.error(MessageFormat.format("[Nio Server Error]<<Request service name:{0}", nta.getRestServiceName() ));
//////			logger.error(MessageFormat.format("[Nio Server Error]<<Request json data:{0}", nta.getJSONdata() ));
//////			logger.error(MessageFormat.format("[Nio Server Error]<<Request dto class:{0}", nta.getClazz() ));
////			MethodExecutePool.runningThreadCount--;
////			return;
////		}
//		String result = "abc";
//		
//		//请求唯一标码
//		String GUID = nta.getGUID();
//		
//		//返回数据，并且设定相同的唯一标码来保证客户端识别是哪次请求
//		NioTransferAdapter rtnNta = new NioTransferAdapter(result);
//		rtnNta.setGUID(GUID);
//		//结果写入nio server
//		session.write(rtnNta);
//		
//		MethodExecutePool.runningThreadCount--;
		//输出log
		//输出请求信息
//		logger.debug(MessageFormat.format("[Nio Server]<<Request service name:{0}", nta.getRestServiceName() ));
//		logger.debug(MessageFormat.format("[Nio Server]<<Request json data:{0}", nta.getJSONdata() ));
//		logger.debug(MessageFormat.format("[Nio Server]<<Request dto class:{0}", nta.getClazz() ));
//		//输出结果信息
//		logger.debug(MessageFormat.format("[Nio Server]>>Response json data:{0}", rtnNta.getJSONdata() ));
//		logger.debug("=============================================================================");
		
	}
	
	
}

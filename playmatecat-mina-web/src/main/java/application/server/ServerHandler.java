package application.server;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import net.minidev.json.JSONValue;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import application.ApplicationContextHolder;
import application.server.thread.MethodCache;

import com.playmatecat.mina.NioTransferAdapter;

/**
 * 服务端消息处理器
 * 
 * @author blackcat
 *
 */
public class ServerHandler extends IoHandlerAdapter {
	
	private static Logger logger = Logger.getLogger(ServerHandler.class);
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		NioTransferAdapter nta = (NioTransferAdapter) message;
//		logger.info(MessageFormat.format("[Nio Server]<<Request service name:{0}", nta.getRestServiceName() ));
//		logger.info(MessageFormat.format("[Nio Server]<<Request json data:{0}", nta.getJSONdata() ));
//		logger.info(MessageFormat.format("[Nio Server]<<Request dto class:{0}", nta.getClazz() ));
		
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
		if(nta.getClazz() != null) {
			//有参调用
			//反射方法用了很多时间！！!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!????!!!
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
		
		//返回数据，并且设定相同的唯一标码来保证客户端识别是哪次请求
		NioTransferAdapter rtnNta = new NioTransferAdapter(result);
		rtnNta.setGUID(GUID);
		session.write(rtnNta);
		
//		ThreadPoolExecutor threadPool = MethodExecutePool.getThreadPool();
//		threadPool.execute(new MethodExecuteRunnable(session, message));
		
//		NioTransferAdapter rtnNta = new NioTransferAdapter("abc");
//		rtnNta.setGUID( ((NioTransferAdapter) message).getGUID() );
//		session.write(rtnNta);
		
		//MethodExecutePool.execute(new MethodExecuteRunnable(session, message));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info(MessageFormat.format("[Nio Server]Session opened.Remote address:{0}", session.getRemoteAddress() ));
		super.sessionOpened(session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info(MessageFormat.format("[Nio Server]Session closed.Remote address:{0}", session.getRemoteAddress() ));
		super.sessionClosed(session);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

}
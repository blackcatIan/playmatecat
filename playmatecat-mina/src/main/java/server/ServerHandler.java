package server;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import net.minidev.json.JSONValue;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import application.ApplicationContextHolder;

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
		logger.info(MessageFormat.format("[Nio Server]<<Request service name:{0}", nta.getRestServiceName() ));
		logger.info(MessageFormat.format("[Nio Server]<<Request json data:{0}", nta.getJSONdata() ));
		logger.info(MessageFormat.format("[Nio Server]<<Request dto class:{0}", nta.getClazz() ));

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
		
		NioTransferAdapter rtnNta = new NioTransferAdapter(result);
		
		session.write(rtnNta);
	
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
		NioTransferAdapter rtnNta = (NioTransferAdapter) message;
		logger.info(MessageFormat.format("[Nio Server]>>Response json data:{0}", rtnNta.getJSONdata() ));
		logger.info("=============================================================================");
		super.messageSent(session, message);
	}

}
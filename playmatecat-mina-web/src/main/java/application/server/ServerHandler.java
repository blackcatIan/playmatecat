package application.server;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import application.server.thread.MethodExecutePool;
import application.server.thread.MethodExecuteRunnable;

import com.playmatecat.mina.NioTransferAdapter;

/**
 * 服务端消息处理器
 * 
 * @author blackcat
 *
 */
public class ServerHandler extends IoHandlerAdapter {
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("session created!");
		super.sessionCreated(session);
	}

	private static Logger logger = Logger.getLogger(ServerHandler.class);
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		//转交给spring多线程扫描器
		MethodExecutePool.execute(new MethodExecuteRunnable(session, message));
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
package client;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.playmatecat.mina.NioTransferAdapter;

public class ClientHandler extends IoHandlerAdapter {
	
	private static Logger logger = Logger.getLogger(ClientHandler.class);


	@Override	
	public void sessionOpened(IoSession session) throws Exception {
		NioTransferAdapter nta = new NioTransferAdapter("abc", "{}", null);
		session.write(nta);
		
		logger.info("[Nio Client]session opened.");
		super.sessionOpened(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		NioTransferAdapter nta = (NioTransferAdapter) message;
		logger.info(MessageFormat.format("[Nio Server]>>Response json data:{0}", nta.getJSONdata() ));
		logger.info("=============================================================================");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		NioTransferAdapter nta = (NioTransferAdapter) message;
		logger.info(MessageFormat.format("[Nio Server]<<Request service name:{0}", nta.getRestServiceName() ));
		logger.info(MessageFormat.format("[Nio Server]<<Request json data:{0}", nta.getJSONdata() ));
		logger.info(MessageFormat.format("[Nio Server]<<Request dto class:{0}", nta.getClazz() ));
		super.messageSent(session, message);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {		
		logger.info("[Nio Client]session closed.");
		super.sessionClosed(session);
	}


}

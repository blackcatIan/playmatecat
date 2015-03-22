package server.thread;

import java.text.MessageFormat;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

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
		
//		FutureTask<String> futureTask = new FutureTask<String>(new MethodFuture(session, message));
//		
//		Thread thread = new Thread(futureTask);
//		thread.start();
//		
//		String result;
//		try {
//			//最大数据请求时间1分钟
//			result = futureTask.get(MAX_METHOD_RUNNING_TIME, TimeUnit.SECONDS);
//		} catch (Exception e) {
//			//处理调用方法超时
////			logger.error(MessageFormat.format("[Nio Server Error]<<Request service name:{0}", nta.getRestServiceName() ));
////			logger.error(MessageFormat.format("[Nio Server Error]<<Request json data:{0}", nta.getJSONdata() ));
////			logger.error(MessageFormat.format("[Nio Server Error]<<Request dto class:{0}", nta.getClazz() ));
//			MethodExecutePool.runningThreadCount--;
//			return;
//		}
		String result = "abc";
		
		//请求唯一标码
		String GUID = nta.getGUID();
		
		//返回数据，并且设定相同的唯一标码来保证客户端识别是哪次请求
		NioTransferAdapter rtnNta = new NioTransferAdapter(result);
		rtnNta.setGUID(GUID);
		//结果写入nio server
		session.write(rtnNta);
		
		MethodExecutePool.runningThreadCount--;
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

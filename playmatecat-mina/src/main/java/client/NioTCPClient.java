package client;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class NioTCPClient {

	private static final String HOSTNAME = "localhost";

	/**服务端口**/
	private static final int PORT = 8501;
	/**服务IP**/
	private static final String ADDRESS = "127.0.0.1";
	/**1分钟超时**/
	private static final int TIME_OUT = 60000;

	/**
	 * 程序入口
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {
		//@STEP1 创建NIO连接器
	    NioSocketConnector connector = new NioSocketConnector();
	    //设定超时值
	    connector.setConnectTimeoutMillis(TIME_OUT);
	    
	    //@STEP2 创建一个过滤器链配置.注意链式顺序
//	    connector.getFilterChain().addLast("logger", new LoggingFilter());
	    
	    //指定数据解析器
//	    connector.getFilterChain().addLast("codec", 
//	    		new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
	    
	    connector.getFilterChain().addLast("codec", 
	    		new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
	    
	    
	    //@STEP3 指定消息处理器
	    connector.setHandler(new ClientHandler());
	    
		// 读取缓冲区
	    connector.getSessionConfig().setReadBufferSize(2048);
		// io空停滞时间
	    connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		// 超时时间
	    connector.getSessionConfig().setWriteTimeout(TIME_OUT);
	    
	    
	    //@STEP4 尝试建立连接,连接成功则跳出循环
	    IoSession session;
	    for (;;) {
	        try {
	            ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
	            future.awaitUninterruptibly();
	            session = future.getSession();
	            break;
	        } catch (RuntimeIoException e) {
	            e.printStackTrace();
	            Thread.sleep(5000);
	        }
	    }
	    
	    //等待所有的请求处理完毕
	    session.getCloseFuture().awaitUninterruptibly();
	    //断开连接
	    connector.dispose();
	}
}

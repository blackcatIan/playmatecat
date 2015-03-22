package server.thread;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sun.nio.ch.ThreadPool;

public class MethodExecutePool {
	private static int consumeTaskSleepTime = 2000;
	private static int produceTaskMaxNumber = 10;
	
	private static int MIN_POOL_SIZE = 2;
	private static int MAX_POOL_SIZE = 100;
	/**有线程闲置10秒则从线程池中减少数量**/
	private static int REDUCE_POOL_TIME = 10;
	
	/**任务缓冲列队**/
	private static int QUEUE_SIZE = 100;
	
	private static ThreadPoolExecutor threadPool;
	
	private MethodExecutePool(){};
	
	static int runningThreadCount = 0;
	
	public static void execute(Runnable runnable) {
		if(runningThreadCount < MAX_POOL_SIZE) {
			Thread thread = new Thread(runnable);
			runningThreadCount++;
			thread.start();
		} else {
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			execute(runnable);
		}
	}
	
	
	public static ThreadPoolExecutor getThreadPool() {
		if(threadPool == null) {
			// 构造一个线程池,任务数量超负荷时,策略为重试加入任务
			threadPool = new ThreadPoolExecutor(MIN_POOL_SIZE, MAX_POOL_SIZE, REDUCE_POOL_TIME,
					TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),
					new ThreadPoolExecutor.CallerRunsPolicy());
		}
		return threadPool;
	}

}

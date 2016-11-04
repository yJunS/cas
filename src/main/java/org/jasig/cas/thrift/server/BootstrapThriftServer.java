package org.jasig.cas.thrift.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.jasig.cas.thrift.server.UserServiceThrift.Processor;

/**
 * @author dongtian
 * @date   2015年5月28日 上午9:18:09
 */
public class BootstrapThriftServer {
	
	private static  Logger logger = Logger.getLogger(BootstrapThriftServer.class);
	
	private UserServiceImplThrift userServiceImplThrift;
	private int port;

	public BootstrapThriftServer(UserServiceImplThrift userServiceImplThrift,
			int port) {
		super();
		this.userServiceImplThrift = userServiceImplThrift;
		this.port = port;
	}


	private TThreadedSelectorServer tThreadedSelectorServer ;
	public void startServe() {
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
		
		fixedThreadPool.submit(new Runnable() {
			@Override
			public void run() {
					
					logger.info("正在启动thrift 服务......");
				UserServiceThrift.Processor<UserServiceThrift.Iface> processor = new Processor<UserServiceThrift.Iface>(userServiceImplThrift); 
				TNonblockingServerTransport nonblockingServerTransport;
				try {
					nonblockingServerTransport = new TNonblockingServerSocket(port);
					TThreadedSelectorServer.Args args3 = new TThreadedSelectorServer.Args(nonblockingServerTransport);
					args3.processor(processor);
					tThreadedSelectorServer = new TThreadedSelectorServer(args3);
						logger.info("成功启动thrift服务");
					tThreadedSelectorServer.serve();
				} catch (TTransportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
	}

	
	public void stopServe () {
		
		if(tThreadedSelectorServer!= null) {
			tThreadedSelectorServer.stop();
		}
	}
}

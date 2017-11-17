package com.liyj.grpc.register;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liyj.grpc.bean.Service;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
/**
 * gRPC服务信息
 * @author liyj-pc
 *
 */
public class GrpcService {
	/**
	 * 服务地址
	 */
	private String host = "localhost";
	/**
	 * 服务端口
	 */
	private int port = 8080;
	/**
	 * 服务使用HTTPS的证书私钥文件地址，不指定将不被支持；
	 */
	private String privateKeyFilePath;
	/**
	 * 服务使用HTTPS的证书链文件地址，不指定将不被支持；
	 */
	private String certChainFilePath;
	/**
	 * 服务列表
	 */
	private List<Service> serviceList;
	/**
	 * gRPC server.
	 */
	private Server server;
	/**
	 * 日志
	 */
	private Logger logger = LoggerFactory.getLogger(GrpcService.class.getName());
	/**
	 * 启动服务
	 */
	public void start() {
		try{
			ServerBuilder<?> builder = ServerBuilder.forPort(port);
			if(privateKeyFilePath != null && certChainFilePath != null 
					&& !privateKeyFilePath.trim().equals("") && !certChainFilePath.trim().equals("")) {
				builder.useTransportSecurity(new File(certChainFilePath), new File(privateKeyFilePath));
			}
			
			for(Service service : serviceList){
				//创建服务类实例
				Class<?> serviceClass = Class.forName(service.getServiceClassName());
				Object serviceInstance = serviceClass.newInstance();
				Object interceptorInstance = null;
				String interceptorClass = service.getInterceptorClassName();
				//创建本服务使用的拦截器
				if(interceptorClass != null && !interceptorClass.trim().equals("")){
					Class<?> valueClass = Class.forName(interceptorClass);
					interceptorInstance = valueClass.newInstance();
				}
				//注册服务
				if(interceptorInstance != null) {
					builder.addService(ServerInterceptors.intercept((BindableService)serviceInstance, 
							(ServerInterceptor)interceptorInstance));
				} else {
					builder.addService(ServerInterceptors.intercept((BindableService)serviceInstance));
				}
			}
			server = builder.build().start();
			logger.info("Server started, listening on " + port);
			//支持优雅停机
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					logger.info("*** Shutting down gRPC server since JVM is shutting down");
					GrpcService.this.stop();
					logger.info("*** Server shut down");
				}
			});
			this.blockUntilShutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("服务异常:", ex);
		}
	}
	/**
	 * 停止服务
	 */
	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}
	/**
	 * 等待停止服务
	 * @throws InterruptedException
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}
	public String getPrivateKeyFilePath() {
		return privateKeyFilePath;
	}
	public void setPrivateKeyFilePath(String privateKeyFilePath) {
		this.privateKeyFilePath = privateKeyFilePath;
	}
	public String getCertChainFilePath() {
		return certChainFilePath;
	}
	public void setCertChainFilePath(String certChainFilePath) {
		this.certChainFilePath = certChainFilePath;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
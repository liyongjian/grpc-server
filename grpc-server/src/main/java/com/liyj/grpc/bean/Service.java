package com.liyj.grpc.bean;

/**
 * 服务定义类，包括实现类和拦截器。
 * @author liyj
 *
 */
public class Service {
	/**
	 * 服务实现类。
	 */
	private String serviceClassName;
	/**
	 * 服务类上使用的拦截器。
	 */
	private String interceptorClassName;
	
	public String getServiceClassName() {
		return serviceClassName;
	}
	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}
	public String getInterceptorClassName() {
		return interceptorClassName;
	}
	public void setInterceptorClassName(String interceptorClassName) {
		this.interceptorClassName = interceptorClassName;
	}
	
}

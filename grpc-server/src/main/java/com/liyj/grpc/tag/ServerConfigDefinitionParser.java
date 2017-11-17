package com.liyj.grpc.tag;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.liyj.grpc.bean.Service;
import com.liyj.grpc.register.GrpcService;

/**
 * serverConfig配置标签的解析。
 * @author liyj-pc
 *
 */
public class ServerConfigDefinitionParser extends AbstractSimpleBeanDefinitionParser{
    /** 
     * element 相当于对应的element元素 parserContext 解析的上下文 builder 用于该标签的实现 
     */  
    @Override  
    protected void doParse(Element element, ParserContext parserContext,  
            BeanDefinitionBuilder builder) {  
    	
        //从标签中取出对应的属性值  
        String host = element.getAttribute("host");  
        String port = element.getAttribute("port");  
        String privateKeyFilePath = element.getAttribute("privateKeyFilePath");  
        String certChainFilePath = element.getAttribute("certChainFilePath");   
        
        // 把创建完的实例对应的传到该标签类实现的相应属性中  
        builder.addPropertyValue("host", host);
        builder.addPropertyValue("port", Integer.parseInt(port));
        builder.addPropertyValue("privateKeyFilePath", privateKeyFilePath);
        builder.addPropertyValue("certChainFilePath", certChainFilePath);
        builder.addPropertyValue("serviceList", parseMapElement(element,  
                parserContext, builder));
        
        builder.setInitMethodName("start");
        builder.setDestroyMethodName("stop");
    }
    @SuppressWarnings("all")  
    public List<Service> parseMapElement(Element mapEle,   
            ParserContext parserContext, BeanDefinitionBuilder builder){
    	List<?> entryEles = DomUtils.getChildElementsByTagName(mapEle, "service");
        //关键是以下这个ManagedList类型，充当着一个list类型的beandefinition类型的说明对象 
        ManagedList list = new ManagedList(entryEles.size());
        list.setMergeEnabled(true);  
        list.setSource(parserContext.getReaderContext().extractSource(mapEle));  
        
        for (Iterator<?> it = entryEles.iterator(); it.hasNext();){
            Element entryEle = (Element) it.next();  
      
            String serviceClassName = entryEle.getAttribute("serviceClassName");  
            String interceptorClassName = entryEle.getAttribute("interceptorClassName");
            Service service = new Service();
            service.setServiceClassName(serviceClassName);
            service.setInterceptorClassName(interceptorClassName);
            list.add(service);
        }
        return list;
    }
    @Override  
    protected Class<?> getBeanClass(Element element) {  
        // 返回该标签所定义的类实现,在这里是为了创建出SqlMapClientTemplate对象  
        return GrpcService.class;
    }
	@Override
	protected String getBeanClassName(Element element) {
		// TODO Auto-generated method stub
		return "GrpcService";
	}
}

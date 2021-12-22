package com.jn.web.config;

//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.stereotype.Component;

/*
 * @Author yaxiongliu
 **/
//@Component
//当Spring容器内没有TomcatEmbeddedServletContainerFactory这个bean时，会吧此bean加载进spring容器中
public class WebServerConfig {
//public class WebServerConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
//    @Override
//    public void customize(ConfigurableWebServerFactory configurableWebServerFactory) {
//        //定制tomcat 连接器对象
//        ((TomcatServletWebServerFactory)configurableWebServerFactory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
//            @Override
//            public void customize(Connector connector) {
//                //获取协议
//                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
//
//                //如果30s没有请求服务的话，则自动断开连接
//                protocol.setKeepAliveTimeout(30000);
//                //最大允许开启10000个链接
//                protocol.setMaxKeepAliveRequests(10);
//            }
//        });
//    }
}

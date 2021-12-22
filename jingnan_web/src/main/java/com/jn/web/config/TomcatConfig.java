package com.jn.web.config;

//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.http11.Http11Nio2Protocol;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

/*
 * @Author yaxiongliu
 **/
//@Configuration
public class TomcatConfig {
//    //自定义SpringBoot嵌入式Tomcat
//    //@Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }
//    //配置连接器
//    //@Bean
//    public Connector httpConnector() {
//        Connector connector=new Connector("org.apache.coyote.http11.Http11Nio2Protocol");
//        Http11Nio2Protocol protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
//        //等待队列最多允许1000个线程在队列中等待
//        protocol.setAcceptCount(1000);
//        // 设置最大线程数
//        protocol.setMaxThreads(800);
//        // 设置最大连接数
//        protocol.setMaxConnections(20000);
//        //定制化keepalivetimeout,设置30秒内没有请求则服务端自动断开keepalive链接
//        //protocol.setKeepAliveTimeout(30000);
//        //当客户端发送超过10000个请求则自动断开keepalive链接
//        //protocol.setMaxKeepAliveRequests(10000);
//        // 请求方式
//        connector.setScheme("http");
//        connector.setPort(9002);                    //自定义的
//        connector.setRedirectPort(8443);
//        return connector;
//    }
}

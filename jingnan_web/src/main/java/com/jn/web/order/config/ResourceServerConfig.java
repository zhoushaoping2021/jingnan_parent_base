package com.jn.web.order.config;



/**
 *
 */
/*@Configuration
@EnableResourceServer
//激活方法上的PreAuthorize注解
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)*/
public class ResourceServerConfig{

    /*@Override
    public void configure(HttpSecurity http) throws Exception {

        //所有请求必须认证通过
        http.authorizeRequests()
                .antMatchers("/wxpay/notify")
                //此处是放开路径 不受权限限制
                .permitAll()
                .anyRequest()
                .authenticated(); //其他地址需要认证授权
    }*/
}

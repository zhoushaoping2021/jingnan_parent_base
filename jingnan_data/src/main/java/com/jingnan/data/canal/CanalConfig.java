package com.jingnan.data.canal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "canal")
@Data
@Component
public class CanalConfig {

    private List<CanalServer> clients;

    @Data
    public static class CanalServer{
        private String hostname;
        private Integer port;
        private String destination;
        private String username;
        private String password;
        private String filter;
    }

}
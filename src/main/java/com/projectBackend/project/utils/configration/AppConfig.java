package com.projectBackend.project.utils.configration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // 3분
        factory.setConnectTimeout(1000 * 60 * 3);

        // Socket Timeout 설정 (in milliseconds)
        factory.setReadTimeout(1000 * 60 * 3);

        return new RestTemplate(factory);
    }
}

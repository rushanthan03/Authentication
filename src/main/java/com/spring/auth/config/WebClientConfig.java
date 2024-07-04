package com.spring.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${notify.base.url}")
    private String notifyServerBaseUrl;

    @Bean
    public WebClient webClientForTextNotificationService() {
        return WebClient
                .builder()
                .baseUrl(notifyServerBaseUrl)
                .build();
    }
    
}

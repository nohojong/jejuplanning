package net.codecraft.jejutrip.common.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        // HttpComponentsClientHttpRequestFactory를 사용하여 GZIP 압축 해제를 자동으로 지원하도록 설정
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        // HttpClient 생성 시 압축 해제를 활성화 (기본적으로 활성화되어 있음)
        CloseableHttpClient httpClient = HttpClients.custom().build();
        requestFactory.setHttpClient(httpClient);

        // User-Agent 헤더 설정은 그대로 유지하는 것이 좋습니다.
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("User-Agent", "Mozilla/5.0"); // 간단하게 설정해도 됨
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}

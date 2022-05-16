package com.microgis.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
public class AppProperties {

    @Value("${device.port}")
    private int serverPort;

    @Value("${prediction.url}")
    private String predictionServerBaseUrl;

    private CacheManager cacheManager;

    public AppProperties(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public void evictCache(String name, String key) {
        Cache cache = cacheManager.getCache(name);
        if (cache != null) {
            cache.evict(key);
        }
    }

    @Bean
    public PropertiesFactoryBean cvlExternalProperties() {
        PropertiesFactoryBean res = new PropertiesFactoryBean();
        res.setFileEncoding("UTF-8");
        res.setLocation(new ClassPathResource("application.properties"));
        return res;
    }
}
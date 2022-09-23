package com.zs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elasticSearch客户端
 * @Created by zs on 2022/5/21.
 */
@Configuration
public class ESClientConfig {

    @Value("${esHost}")
    private String esHost;

    @Value("${esPort}")
    private int esPort;

    @Value("${esScheme}")
    private String esScheme;

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {
        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost(esHost, esPort, esScheme));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(clientBuilder);
        return restHighLevelClient;
    }

}

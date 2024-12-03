package com.wsws.moduleexternalapi.feed.config;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
@RequiredArgsConstructor
public class RedisVectorStoreConfig {

    @Value("${spring.ai.vectorstore.redis.uri}")
    private String redisHost;

    @Value("${spring.ai.vectorstore.redis.port}")
    private int redisPort;

    @Value("${spring.ai.vectorstore.redis.index}")
    private String redisIndex;

    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String redisPredix;



    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled(redisHost, redisPort);
    }

    @Bean
    public RedisVectorStore redisVectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        RedisVectorStore.RedisVectorStoreConfig config = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withIndexName(redisIndex)
                .withPrefix(redisPredix)
                .build();

        return new RedisVectorStore(config, embeddingModel, jedisPooled, true);
    }
}
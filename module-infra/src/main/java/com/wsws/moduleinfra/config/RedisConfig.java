package com.wsws.moduleinfra.config;

import com.wsws.moduleinfra.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port); // 이거는
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Key 직렬화 설정
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // Value 직렬화 설정
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplateInteger(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    // Redis 메시지 리스너 설정
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListenerAdapter,
            ChannelTopic channelTopic) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, channelTopic);
        return redisMessageListenerContainer;
    }

    // 메시지를 처리하는 리스너 어댑터 설정
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    // 구독할 Redis 채널 설정
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("/sub/chat");
    }

    // Object 타입 데이터를 처리하는 RedisTemplate 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplateObject(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}

package com.wsws.moduleapplication.notification.producer;

import com.wsws.moduleexternalapi.fcm.config.RabbitMQConfig;
import com.wsws.moduleexternalapi.fcm.dto.FcmRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(FcmRequestDto dto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, dto);
        log.info("fcm 메시지 RabbitMQ에 저장: {}",dto);
    }
}

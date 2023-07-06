package org.zgrinberg.tracing.microservicespring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationsServiceKafka implements NotificationsService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${custom.communication.topicName}")
    private String topicName;



    @Override
    public void sendNotification(String carId) {
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(topicName, String.format("Car with id %s just inserted to DB", carId));
        send.whenComplete((result,ex) -> {
            if(ex == null)
            {
                log.info(String.format("Sent Message of carId=%s, with offset=%s to kafka topic= %s",carId,result.getRecordMetadata().offset()), result.getRecordMetadata().topic());
            }
            else {
            log.error("Couldn't send message about notification of insertion to DB of car with id=" + carId + ", error details: " + ex.getMessage());
            }
        });

    }
}

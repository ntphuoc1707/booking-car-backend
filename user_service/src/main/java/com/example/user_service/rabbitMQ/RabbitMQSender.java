package com.example.user_service.rabbitMQ;

import com.example.user_service.model.Pack4RBMQ;
import com.example.user_service.model.Result;
import com.google.gson.Gson;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * com.example.user_service.rabbitMQ;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 09:56 CH
 * Description: ...
 */
@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    String routingkey;

    public Result sendMessage2UserService(Pack4RBMQ pack4RBMQ) {

        //LOGGER.info(String.format("Json message sent -> %s", user.toString()));
        return (Result) rabbitTemplate.convertSendAndReceive(exchange, routingkey, pack4RBMQ);
    }

    public Result sendMessage2FirebaseService(Pack4RBMQ pack4RBMQ) {
        String routingKey="routingKey.firebase";
        //LOGGER.info(String.format("Json message sent -> %s", user.toString()));
        return new Gson().fromJson(String.valueOf(rabbitTemplate.convertSendAndReceive(exchange, routingKey, pack4RBMQ)),Result.class);
    }
}

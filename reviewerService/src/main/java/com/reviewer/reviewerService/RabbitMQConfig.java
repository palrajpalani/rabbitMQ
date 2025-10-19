package com.reviewer.reviewerService;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String REVIEW_QUEUE = "reviewQueue";

    @Bean
    public Queue reviewQueue() {
        return new Queue(REVIEW_QUEUE, false);
    }
}

package com.reviewer.reviewerService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RabbitTemplate rabbitTemplate;

    public ReviewService(ReviewRepository reviewRepository, RabbitTemplate rabbitTemplate) {
        this.reviewRepository = reviewRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Review addReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        rabbitTemplate.convertAndSend(RabbitMQConfig.REVIEW_QUEUE, savedReview);
        return savedReview;
    }
}

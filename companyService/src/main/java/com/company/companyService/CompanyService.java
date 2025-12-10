package com.company.companyService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @RabbitListener(queues = "reviewQueue", containerFactory = "rabbitListenerContainerFactory")
    public void receiveReviewMessage(ReviewMessage reviewMessage) {
        companyRepository.findById(reviewMessage.getCompanyId()).ifPresentOrElse(company -> {
            int newCount = company.getRatingCount() + 1;
            double newAvgRating = (company.getAverageRating() * company.getRatingCount() + reviewMessage.getRating()) / newCount;
            company.setAverageRating(newAvgRating);
            System.out.println("New Average Rating: " + newAvgRating);
            company.setRatingCount(newCount);
            companyRepository.save(company);
        }, () -> {
            // Create new company record if it doesn't exist
            Company newCompany = new Company();
            newCompany.setId(reviewMessage.getCompanyId());
            newCompany.setName("Unknown Company"); // Set default or fetch from another service
            newCompany.setAverageRating(reviewMessage.getRating());
            newCompany.setRatingCount(1);
            companyRepository.save(newCompany);
        });

        System.out.println("Received Review Message: " + reviewMessage);
    }
}

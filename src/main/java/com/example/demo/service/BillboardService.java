package com.example.demo.service;

import com.example.demo.Repo.BillboardRepo;
import com.example.demo.models.Billboard;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;



@Service
public class BillboardService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BillboardRepo billboardRepo;

    public BillboardService(BillboardRepo billboardRepo) {
        this.billboardRepo = billboardRepo;
    }
    @Scheduled(cron = "* * * * * *")
    public void updateExpiredStatus() {
        Iterable<Billboard> billboards = billboardRepo.findAll();
        LocalDate currentDate = LocalDate.now();

        for (Billboard billboard : billboards) {
            if (billboard.getEndDate1().isBefore(currentDate)) {
                billboard.setStatus("expired");
                billboardRepo.save(billboard);
                logger.info("Billboard {} expired", billboard.getId());
            }
        }
    }
    public List<Billboard> searchItems(String username, String address, String price, String type, LocalDate startDate1, LocalDate endDate1, String status) {
        List<Billboard> searchResults = new ArrayList<>();

        if (username != null && !username.isEmpty()) {
            searchResults.addAll(billboardRepo.findByClientUsername(username));
        }

        if (address != null && !address.isEmpty()) {
            searchResults.addAll(billboardRepo.findByAddress(address));
        }

        if (price != null && !price.isEmpty()) {
            searchResults.addAll(billboardRepo.findByPrice(price));
        }

        if (type != null && !type.isEmpty()) {
            searchResults.addAll(billboardRepo.findByType(type));
        }

        if (startDate1 != null && endDate1 != null) {
            searchResults.addAll(billboardRepo.findByStartDateAndEndDate(startDate1, endDate1));
        }

        if (status != null && !status.isEmpty()) {
            searchResults.addAll(billboardRepo.findByStatus(status));
        }
        return searchResults;
    }
}

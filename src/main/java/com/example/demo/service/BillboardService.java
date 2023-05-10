package com.example.demo.service;

import com.example.demo.Repo.BillboardRepo;
import com.example.demo.models.Billboard;
import com.example.demo.models.User;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

import javax.persistence.criteria.*;


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
//public List<Billboard> searchItems(String username, String address, String price, String type,
//                                   LocalDate startDate, LocalDate endDate, String status) {
//    CriteriaBuilder cb = billboardRepo.getCriteriaBuilder();
//    CriteriaQuery<Billboard> query = cb.createQuery(Billboard.class);
//    Root<Billboard> root = query.from(Billboard.class);
//
//    List<Predicate> predicates = new ArrayList<>();
//
//    // Добавляем условия поиска в список предикатов
//    if (username != null && !username.isEmpty()) {
//        Join<Billboard, User> userJoin = root.join("client");
//        predicates.add(cb.equal(userJoin.get("username"), username));
//    }
//
//    if (address != null && !address.isEmpty()) {
//        predicates.add(cb.equal(root.get("address"), address));
//    }
//
//    if (price != null && !price.isEmpty()) {
//        predicates.add(cb.equal(root.get("price"), price));
//    }
//
//    if (type != null && !type.isEmpty()) {
//        predicates.add(cb.equal(root.get("type"), type));
//    }
//
//    if (startDate != null) {
//        predicates.add(cb.greaterThanOrEqualTo(root.get("startDate1"), startDate));
//    }
//
//    if (endDate != null) {
//        predicates.add(cb.lessThanOrEqualTo(root.get("endDate1"), endDate));
//    }
//
//    if (status != null && !status.isEmpty()) {
//        predicates.add(cb.equal(root.get("status"), status));
//    }
//
//    // Собираем все предикаты с помощью логического И
//    query.where(cb.and(predicates.toArray(new Predicate[0])));
//
//    return billboardRepo.findAll(query);
//}
}

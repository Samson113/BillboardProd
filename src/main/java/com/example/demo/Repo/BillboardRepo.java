package com.example.demo.Repo;

import com.example.demo.models.Billboard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BillboardRepo extends CrudRepository<Billboard, Long> {
    @Query("SELECT COUNT(b) FROM Billboard b WHERE b.status = :status AND b.inWork = true")
    int countByStatusAndInWorkIsTrue(@Param("status") String status);

    List<Billboard> findAllByEndDateBeforeAndStatus(LocalDate currentDate, String status);

    @Query("SELECT b FROM Billboard b WHERE b.status = :status")
    List<Billboard> findAllByStatus(@Param("status") String status);
    @Query("SELECT e FROM Billboard e WHERE e.client.id = ?1")
    List<Billboard> findByClientId(Long clientId);
    @Query("SELECT f FROM Billboard f WHERE f.client.id = ?1 AND f.status = ?2")
    List<Billboard> findByClientIdAndStatus(Long clientId, @Param("status") String status);

}

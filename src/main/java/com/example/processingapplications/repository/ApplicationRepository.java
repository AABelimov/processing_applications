package com.example.processingapplications.repository;

import com.example.processingapplications.entity.Application;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUserId(Long userId, PageRequest pageRequest);

    List<Application> findAllByStatus(String status, PageRequest pageRequest);

    @Query("SELECT a FROM Application a, User u WHERE a.user = u AND a.status = ?1 AND u.name LIKE %?2%")
    List<Application> findAllByStatusAndName(String status, String name, PageRequest pageRequest);
}

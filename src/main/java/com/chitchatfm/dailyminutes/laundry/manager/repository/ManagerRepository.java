package com.chitchatfm.dailyminutes.laundry.manager.repository;
import com.chitchatfm.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
}


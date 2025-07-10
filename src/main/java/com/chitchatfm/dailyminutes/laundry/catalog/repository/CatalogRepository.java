package com.chitchatfm.dailyminutes.laundry.catalog.repository;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<CatalogEntity, Long> {
}


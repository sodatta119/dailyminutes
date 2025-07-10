package com.chitchatfm.dailyminutes.laundry.geofence.repository;

import com.chitchatfm.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeofenceRepository extends JpaRepository<GeofenceEntity, Long> {
    List<GeofenceEntity> findByStoreId(Long storeId);
}


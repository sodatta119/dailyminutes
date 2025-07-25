/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.service;


import com.dailyminutes.laundry.geofence.dto.GeofenceCustomerSummaryResponse;
import com.dailyminutes.laundry.geofence.dto.GeofenceOrderSummaryResponse;
import com.dailyminutes.laundry.geofence.dto.GeofenceResponse;
import com.dailyminutes.laundry.geofence.dto.GeofenceStoreSummaryResponse;
import com.dailyminutes.laundry.geofence.repository.GeofenceCustomerSummaryRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceOrderSummaryRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceStoreSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeofenceQueryService {

    private final GeofenceRepository geofenceRepository;
    private final GeofenceStoreSummaryRepository storeSummaryRepository;
    private final GeofenceCustomerSummaryRepository customerSummaryRepository;
    private final GeofenceOrderSummaryRepository orderSummaryRepository;

    public Optional<GeofenceResponse> findGeofenceById(Long id) {
        return geofenceRepository.findById(id)
                .map(g -> new GeofenceResponse(g.getId(), g.getPolygonCoordinates(), g.getGeofenceType(), g.getName(), g.isActive()));
    }

    public List<GeofenceResponse> findAllGeofences() {
        return StreamSupport.stream(geofenceRepository.findAll().spliterator(), false)
                .map(g -> new GeofenceResponse(g.getId(), g.getPolygonCoordinates(), g.getGeofenceType(), g.getName(), g.isActive()))
                .collect(Collectors.toList());
    }

    public List<GeofenceStoreSummaryResponse> findStoreSummariesByGeofenceId(Long geofenceId) {
        return storeSummaryRepository.findByGeofenceId(geofenceId).stream()
                .map(s -> new GeofenceStoreSummaryResponse(s.getId(), s.getStoreId(), s.getGeofenceId(), s.getStoreName(), s.getStoreAddress()))
                .collect(Collectors.toList());
    }

    public List<GeofenceCustomerSummaryResponse> findCustomerSummariesByGeofenceId(Long geofenceId) {
        return customerSummaryRepository.findByGeofenceId(geofenceId).stream()
                .map(s -> new GeofenceCustomerSummaryResponse(s.getId(), s.getCustomerId(), s.getGeofenceId(), s.getCustomerName(), s.getCustomerPhoneNumber()))
                .collect(Collectors.toList());
    }

    public List<GeofenceOrderSummaryResponse> findOrderSummariesByGeofenceId(Long geofenceId) {
        return orderSummaryRepository.findByGeofenceId(geofenceId).stream()
                .map(s -> new GeofenceOrderSummaryResponse(s.getId(), s.getOrderId(), s.getGeofenceId(), s.getOrderDate(), s.getStatus(), s.getTotalAmount(), s.getCustomerId(), s.getStoreId()))
                .collect(Collectors.toList());
    }
}

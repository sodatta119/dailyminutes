/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.service;


import com.dailyminutes.laundry.geofence.dto.GeofenceCustomerSummaryResponse;
import com.dailyminutes.laundry.geofence.dto.GeofenceOrderSummaryResponse;
import com.dailyminutes.laundry.geofence.dto.GeofenceResponse;
import com.dailyminutes.laundry.geofence.repository.GeofenceCustomerSummaryRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceOrderSummaryRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Geofence query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeofenceQueryService {

    private final GeofenceRepository geofenceRepository;
    private final GeofenceCustomerSummaryRepository customerSummaryRepository;
    private final GeofenceOrderSummaryRepository orderSummaryRepository;

    /**
     * Find geofence by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<GeofenceResponse> findGeofenceById(Long id) {
        return geofenceRepository.findById(id)
                .map(g -> new GeofenceResponse(g.getId(), g.getPolygonCoordinates(), g.getGeofenceType(), g.getName(), g.isActive()));
    }

    /**
     * Find all geofences list.
     *
     * @return the list
     */
    public List<GeofenceResponse> findAllGeofences() {
        return StreamSupport.stream(geofenceRepository.findAll().spliterator(), false)
                .map(g -> new GeofenceResponse(g.getId(), g.getPolygonCoordinates(), g.getGeofenceType(), g.getName(), g.isActive()))
                .collect(Collectors.toList());
    }

    /**
     * Find customer summaries by geofence id list.
     *
     * @param geofenceId the geofence id
     * @return the list
     */
    public List<GeofenceCustomerSummaryResponse> findCustomerSummariesByGeofenceId(Long geofenceId) {
        return customerSummaryRepository.findByGeofenceId(geofenceId).stream()
                .map(s -> new GeofenceCustomerSummaryResponse(s.getId(), s.getCustomerId(), s.getGeofenceId(), s.getCustomerName(), s.getCustomerPhoneNumber()))
                .collect(Collectors.toList());
    }

    /**
     * Find order summaries by geofence id list.
     *
     * @param geofenceId the geofence id
     * @return the list
     */
    public List<GeofenceOrderSummaryResponse> findOrderSummariesByGeofenceId(Long geofenceId) {
        return orderSummaryRepository.findByGeofenceId(geofenceId).stream()
                .map(s -> new GeofenceOrderSummaryResponse(s.getId(), s.getOrderId(), s.getGeofenceId(), s.getOrderDate(), s.getStatus(), s.getTotalAmount(), s.getCustomerId(), s.getStoreId()))
                .collect(Collectors.toList());
    }
}

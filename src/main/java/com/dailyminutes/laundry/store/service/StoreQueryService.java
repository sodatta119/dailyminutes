/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.service;

import com.dailyminutes.laundry.store.dto.*;
import com.dailyminutes.laundry.store.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Store query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreQueryService {

    private final StoreRepository storeRepository;
    private final StoreAgentSummaryRepository agentSummaryRepository;
    private final StoreGeofenceSummaryRepository geofenceSummaryRepository;
    private final StoreOrderSummaryRepository orderSummaryRepository;
    private final StoreTaskSummaryRepository taskSummaryRepository;

    /**
     * Find store by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<StoreResponse> findStoreById(Long id) {
        return storeRepository.findById(id)
                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getContactNumber(), s.getEmail(), s.getManagerId(), s.getLatitude(), s.getLongitude()));
    }

    /**
     * Find all stores list.
     *
     * @return the list
     */
    public List<StoreResponse> findAllStores() {
        return StreamSupport.stream(storeRepository.findAll().spliterator(), false)
                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getContactNumber(), s.getEmail(), s.getManagerId(), s.getLatitude(), s.getLongitude()))
                .collect(Collectors.toList());
    }

    /**
     * Find agent summaries by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    public List<StoreAgentSummaryResponse> findAgentSummariesByStoreId(Long storeId) {
        return agentSummaryRepository.findByStoreId(storeId).stream()
                .map(s -> new StoreAgentSummaryResponse(s.getId(), s.getStoreId(), s.getAgentId(), s.getAgentName(), s.getAgentPhoneNumber(), s.getAgentDesignation(), s.getAgentStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Find geofence summaries by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    public List<StoreGeofenceSummaryResponse> findGeofenceSummariesByStoreId(Long storeId) {
        return geofenceSummaryRepository.findByStoreId(storeId).stream()
                .map(s -> new StoreGeofenceSummaryResponse(s.getId(), s.getStoreId(), s.getGeofenceId(), s.getGeofenceName(), s.getGeofenceType(), s.isActive()))
                .collect(Collectors.toList());
    }

    /**
     * Find order summaries by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    public List<StoreOrderSummaryResponse> findOrderSummariesByStoreId(Long storeId) {
        return orderSummaryRepository.findByStoreId(storeId).stream()
                .map(s -> new StoreOrderSummaryResponse(s.getId(), s.getStoreId(), s.getOrderId(), s.getOrderDate(), s.getStatus(), s.getTotalAmount(), s.getCustomerId()))
                .collect(Collectors.toList());
    }

    /**
     * Find task summaries by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    public List<StoreTaskSummaryResponse> findTaskSummariesByStoreId(Long storeId) {
        return taskSummaryRepository.findByStoreId(storeId).stream()
                .map(s -> new StoreTaskSummaryResponse(s.getId(), s.getStoreId(), s.getTaskId(), s.getTaskType(), s.getTaskStatus(), s.getTaskStartTime(), s.getAgentId(), s.getAgentName(), s.getOrderId()))
                .collect(Collectors.toList());
    }
}

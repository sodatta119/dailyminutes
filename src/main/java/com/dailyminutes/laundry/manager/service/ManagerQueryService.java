/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.service;

import com.dailyminutes.laundry.manager.dto.ManagerResponse;
import com.dailyminutes.laundry.manager.repository.ManagerRepository;
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
public class ManagerQueryService {

    private final ManagerRepository managerRepository;

    public Optional<ManagerResponse> findManagerById(Long id) {
        return managerRepository.findById(id)
                .map(m -> new ManagerResponse(m.getId(), m.getName(), m.getContact()));
    }

    public List<ManagerResponse> findAllManagers() {
        return StreamSupport.stream(managerRepository.findAll().spliterator(), false)
                .map(m -> new ManagerResponse(m.getId(), m.getName(), m.getContact()))
                .collect(Collectors.toList());
    }
}

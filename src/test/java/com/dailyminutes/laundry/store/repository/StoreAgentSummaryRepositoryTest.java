package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.store.domain.model.StoreAgentSummaryEntity;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.store.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.store.domain.model") // Updated package name
class StoreAgentSummaryRepositoryTest {

    @Autowired
    private StoreAgentSummaryRepository storeAgentSummaryRepository;

    @Autowired
    private StoreRepository storeRepository;


    private String generateUniquePhoneNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    @Test
    void testSaveAndFindStoreAgentSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        String phoneNumber = generateUniquePhoneNumber();

        StoreAgentSummaryEntity summary = new StoreAgentSummaryEntity(
                null, store.getId(), 10l, "Agent Alpha", phoneNumber, "FLEET_AGENT", "ACTIVE");
        StoreAgentSummaryEntity savedSummary = storeAgentSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getStoreId()).isEqualTo(store.getId());
        assertThat(savedSummary.getAgentId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentName()).isEqualTo("Agent Alpha");
        assertThat(savedSummary.getAgentPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedSummary.getAgentDesignation()).isEqualTo("FLEET_AGENT");
        assertThat(savedSummary.getAgentStatus()).isEqualTo("ACTIVE");

        Optional<StoreAgentSummaryEntity> foundSummary = storeAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent Alpha");
    }

    @Test
    void testUpdateStoreAgentSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        String phoneNumber = generateUniquePhoneNumber();

        StoreAgentSummaryEntity summary = new StoreAgentSummaryEntity(
                null, store.getId(), 10l, "Agent Beta", phoneNumber, "DELIVERY_EXECUTIVE", "ACTIVE");
        StoreAgentSummaryEntity savedSummary = storeAgentSummaryRepository.save(summary);

        savedSummary.setAgentStatus("INACTIVE");
        storeAgentSummaryRepository.save(savedSummary);

        Optional<StoreAgentSummaryEntity> updatedSummary = storeAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getAgentStatus()).isEqualTo("INACTIVE");
    }

    @Test
    void testDeleteStoreAgentSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        String phoneNumber = generateUniquePhoneNumber();

        StoreAgentSummaryEntity summary = new StoreAgentSummaryEntity(
                null, store.getId(), 10l, "Agent Gamma", phoneNumber, "PROCESS_MANAGER", "ACTIVE");
        StoreAgentSummaryEntity savedSummary = storeAgentSummaryRepository.save(summary);

        storeAgentSummaryRepository.deleteById(savedSummary.getId());
        Optional<StoreAgentSummaryEntity> deletedSummary = storeAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store1.getId(), 10l, "Agent D", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store1.getId(), 20l, "Agent E", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store2.getId(), 30l, "Agent F", generateUniquePhoneNumber(), "PROCESS_MANAGER", "ACTIVE"));

        List<StoreAgentSummaryEntity> summaries = storeAgentSummaryRepository.findByStoreId(store1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(store1.getId()))).isTrue();
    }

    @Test
    void testFindByAgentId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store1.getId(), 10l, "Agent G", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store2.getId(), 20l, "Agent H", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));

        Optional<StoreAgentSummaryEntity> foundSummary = storeAgentSummaryRepository.findByAgentId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent G");
    }

    @Test
    void testFindByAgentDesignation() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store1.getId(), 10l, "Agent I", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store2.getId(), 20l, "Agent J", generateUniquePhoneNumber(), "FLEET_AGENT", "INACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store3.getId(), 30l, "Agent K", generateUniquePhoneNumber(), "PROCESS_MANAGER", "ACTIVE"));

        List<StoreAgentSummaryEntity> fleetAgents = storeAgentSummaryRepository.findByAgentDesignation("FLEET_AGENT");
        assertThat(fleetAgents).hasSize(2);
        assertThat(fleetAgents.stream().allMatch(s -> s.getAgentDesignation().equals("FLEET_AGENT"))).isTrue();
    }

    @Test
    void testFindByAgentStatus() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store1.getId(), 10l, "Agent L", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store2.getId(), 20l, "Agent M", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));
        storeAgentSummaryRepository.save(new StoreAgentSummaryEntity(null, store3.getId(), 30l, "Agent N", generateUniquePhoneNumber(), "PROCESS_MANAGER", "INACTIVE"));

        List<StoreAgentSummaryEntity> activeAgents = storeAgentSummaryRepository.findByAgentStatus("ACTIVE");
        assertThat(activeAgents).hasSize(2);
        assertThat(activeAgents.stream().allMatch(s -> s.getAgentStatus().equals("ACTIVE"))).isTrue();
    }

}

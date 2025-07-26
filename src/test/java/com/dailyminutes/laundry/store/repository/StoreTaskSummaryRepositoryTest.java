package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.domain.model.StoreTaskSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.store.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.store.domain.model") // Updated package name
class StoreTaskSummaryRepositoryTest {

    @Autowired
    private StoreTaskSummaryRepository storeTaskSummaryRepository;

    @Autowired
    private StoreRepository storeRepository;


    @Test
    void testSaveAndFindStoreTaskSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));

        StoreTaskSummaryEntity summary = new StoreTaskSummaryEntity(null, store.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent Alpha", 10l);
        StoreTaskSummaryEntity savedSummary = storeTaskSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getStoreId()).isEqualTo(store.getId());
        assertThat(savedSummary.getTaskId()).isEqualTo(10l);
        assertThat(savedSummary.getTaskType()).isEqualTo("PICKUP");
        assertThat(savedSummary.getTaskStatus()).isEqualTo("NEW");
        assertThat(savedSummary.getAgentId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentName()).isEqualTo("Agent Alpha");
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);

        Optional<StoreTaskSummaryEntity> foundSummary = storeTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskType()).isEqualTo("PICKUP");
    }

    @Test
    void testUpdateStoreTaskSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));

        StoreTaskSummaryEntity summary = new StoreTaskSummaryEntity(null, store.getId(), 10l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 10l, "Agent Beta", 10l);
        StoreTaskSummaryEntity savedSummary = storeTaskSummaryRepository.save(summary);

        savedSummary.setTaskStatus("SUCCESSFUL");
        savedSummary.setAgentName("Agent Beta (Completed)");
        storeTaskSummaryRepository.save(savedSummary);

        Optional<StoreTaskSummaryEntity> updatedSummary = storeTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTaskStatus()).isEqualTo("SUCCESSFUL");
        assertThat(updatedSummary.get().getAgentName()).isEqualTo("Agent Beta (Completed)");
    }

    @Test
    void testDeleteStoreTaskSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));

        StoreTaskSummaryEntity summary = new StoreTaskSummaryEntity(null, store.getId(), 10l, "PROCESS", "STARTED", LocalDateTime.now(), 10l, "Agent Gamma", 10l);
        StoreTaskSummaryEntity savedSummary = storeTaskSummaryRepository.save(summary);

        storeTaskSummaryRepository.deleteById(savedSummary.getId());
        Optional<StoreTaskSummaryEntity> deletedSummary = storeTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent D", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 20l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 20l, "Agent E", 20l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store2.getId(), 30l, "PROCESS", "STARTED", LocalDateTime.now(), 30l, "Agent F", 30l));

        List<StoreTaskSummaryEntity> summaries = storeTaskSummaryRepository.findByStoreId(store1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(store1.getId()))).isTrue();
    }

    @Test
    void testFindByTaskId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 10l, "PROCESS", "COMPLETED", LocalDateTime.now(), 10l, "Agent G", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store2.getId(), 20l, "PICKUP", "NEW", LocalDateTime.now(), 20l, "Agent H", 20l));

        Optional<StoreTaskSummaryEntity> foundSummary = storeTaskSummaryRepository.findByTaskId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testFindByAgentId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 10l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 10l, "Agent I", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store2.getId(), 20l, "PICKUP", "STARTED", LocalDateTime.now(), 10l, "Agent J", 20l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store3.getId(), 30l, "PROCESS", "NEW", LocalDateTime.now(), 30l, "Agent K", 30l));

        List<StoreTaskSummaryEntity> summariesForAgent = storeTaskSummaryRepository.findByAgentId(10l);
        assertThat(summariesForAgent).hasSize(2);
        assertThat(summariesForAgent.stream().allMatch(s -> s.getAgentId().equals(10l))).isTrue();
    }

    @Test
    void testFindByTaskStatus() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent L", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store2.getId(), 20l, "DELIVERY", "NEW", LocalDateTime.now(), 20l, "Agent M", 20l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store3.getId(), 30l, "PROCESS", "ASSIGNED", LocalDateTime.now(), 30l, "Agent N", 30l));

        List<StoreTaskSummaryEntity> newTasks = storeTaskSummaryRepository.findByTaskStatus("NEW");
        assertThat(newTasks).hasSize(2);
        assertThat(newTasks.stream().allMatch(s -> s.getTaskStatus().equals("NEW"))).isTrue();
    }

    @Test
    void testFindByTaskType() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent O", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store2.getId(), 20l, "PICKUP", "ASSIGNED", LocalDateTime.now(), 20l, "Agent P", 20l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store3.getId(), 30l, "DELIVERY", "STARTED", LocalDateTime.now(), 30l, "Agent Q", 30l));

        List<StoreTaskSummaryEntity> pickupTasks = storeTaskSummaryRepository.findByTaskType("PICKUP");
        assertThat(pickupTasks).hasSize(2);
        assertThat(pickupTasks.stream().allMatch(s -> s.getTaskType().equals("PICKUP"))).isTrue();
    }

    @Test
    void testFindByOrderId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent R", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store2.getId(), 20l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 20l, "Agent S", 10l));
        storeTaskSummaryRepository.save(new StoreTaskSummaryEntity(null, store3.getId(), 30l, "PROCESS", "STARTED", LocalDateTime.now(), 30l, "Agent T", 20l));

        List<StoreTaskSummaryEntity> tasksForOrder = storeTaskSummaryRepository.findByOrderId(10l);
        assertThat(tasksForOrder).hasSize(2);
        assertThat(tasksForOrder.stream().allMatch(s -> s.getOrderId().equals(10l))).isTrue();
    }
}

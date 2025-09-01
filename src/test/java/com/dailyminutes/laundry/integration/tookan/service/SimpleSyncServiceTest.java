package com.dailyminutes.laundry.integration.tookan.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * The type Simple sync service test.
 */
@ExtendWith(MockitoExtension.class)
class SimpleSyncServiceTest {

//    @Mock
//    TookanSyncClient tookanClient;
//    @Mock
//    TeamRepository teamRepository;
//
//    @InjectMocks
//    SimpleSyncService service;
//
//    @Test
//    void syncTeams_inserts_whenNotExisting() {
//        // Given Tookan returns two teams that don't exist locally
//        var rec1 = new TookanTeamRecord("", 2, 123, "Team A", "SF", "37.1", "-122.1");
//        var rec2 = new TookanTeamRecord("", 2, 456, "Team B", "Oakland", "37.2", "-122.2");
//
//        var existing1 = new TeamEntity();
//        existing1.setId(1L);
//        existing1.setExternalId("789");
//        existing1.setName("Team C");
//        var existing2 = new TeamEntity();
//        existing2.setId(1L);
//        existing2.setExternalId("790");
//        existing2.setName("Team D");
//
//        when(tookanClient.listTeams()).thenReturn(List.of(rec1, rec2));
//        when(teamRepository.findByExternalId("123")).thenReturn(Optional.empty());
//        when(teamRepository.findByExternalId("456")).thenReturn(Optional.empty());
//        when(teamRepository.findAll()).thenReturn(List.of(existing1, existing2));
//
//        // When
//        List<TeamEntity> teams = service.syncTeams();
//
//        // Then
//        assertEquals(2, teams.size(), "should upsert 2 teams");
//
//        // capture entity saves to verify fields set
//        ArgumentCaptor<TeamEntity> captor = ArgumentCaptor.forClass(TeamEntity.class);
//        verify(teamRepository, times(2)).save(captor.capture());
//
//        var saved = captor.getAllValues();
//        // Order of saves may follow iteration; check contents regardless
//        var ids = saved.stream().map(TeamEntity::getExternalId).sorted().toList();
//        var names = saved.stream().map(TeamEntity::getName).sorted().toList();
//
//        assertEquals(List.of("123", "456"), ids);
//        assertEquals(List.of("Team A", "Team B"), names);
//
//        verify(teamRepository).findByExternalId("123");
//        verify(teamRepository).findByExternalId("456");
//        verifyNoMoreInteractions(teamRepository);
//    }
//
//    @Test
//    void syncTeams_updates_whenExisting() {
//        // Given one existing team that should be updated
//        var rec = new TookanTeamRecord("", 2, 789, "Team C (Renamed)", "", "", "");
//        var existing = new TeamEntity();
//        existing.setId(1L);
//        existing.setExternalId("789");
//        existing.setName("Team C");
//
//        when(tookanClient.listTeams()).thenReturn(List.of(rec));
//        when(teamRepository.findByExternalId("789")).thenReturn(Optional.of(existing));
//        when(teamRepository.findAll()).thenReturn(List.of(existing));
//
//        List<TeamEntity> teams = service.syncTeams();
//
//        assertEquals(1, teams.size(), "should upsert 1 team");
//        // verify that save() was called with the same entity, and name updated
//        ArgumentCaptor<TeamEntity> captor = ArgumentCaptor.forClass(TeamEntity.class);
//        verify(teamRepository).save(captor.capture());
//
//        TeamEntity saved = captor.getValue();
//        assertEquals("789", saved.getExternalId());
//        assertEquals("Team C (Renamed)", saved.getName());
//
//        verify(teamRepository).findByExternalId("789");
//        verifyNoMoreInteractions(teamRepository);
//    }
//
//    @Test
//    void syncTeams_noData_returnsZero() {
//        when(tookanClient.listTeams()).thenReturn(List.of());
//        //when(teamRepository.findAll()).thenReturn(List.of());
//        List<TeamEntity> teams = service.syncTeams();
//        assertEquals(0, teams.size());
//        //verifyNoInteractions(teamRepository);
//    }
}
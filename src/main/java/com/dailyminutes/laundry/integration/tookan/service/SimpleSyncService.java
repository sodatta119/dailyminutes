/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.service;

import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import com.dailyminutes.laundry.integration.tookan.client.SimpleTookanClient;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// com.dailyminutes.laundry.integration.tookan.SimpleSyncService.java
@Service
@RequiredArgsConstructor
public class SimpleSyncService {
    private final SimpleTookanClient client;
    private final TeamRepository teamRepo;
    private final AgentRepository agentRepo;
    private final GeofenceRepository geofenceRepo;

    public int syncTeams() {
        var rows = client.listTeams();
        int upserts = 0;
        for (var r : rows) {
            String tookanId = String.valueOf(r.get("id"));
            String name = (String) r.get("name");

            var existing = teamRepo.findByExternalId(tookanId);
            if (existing.isPresent()) {
                var e = existing.get();
                e.setName(name);
                teamRepo.save(e);
            } else {
                var e = new TeamEntity();
                e.setExternalId(tookanId);
                e.setName(name);
                teamRepo.save(e);
            }
            upserts++;
        }
        return upserts;
    }

    public int syncAgents() {
        var rows = client.listAgents();
        int upserts = 0;
        for (var r : rows) {
            String tookanId = String.valueOf(r.get("id"));
            String name = (String) r.get("name");
            String phone = (String) r.get("phone");
            String email = (String) r.get("email");

            var existing = agentRepo.findByExternalId(tookanId);
            if (existing.isPresent()) {
                var e = existing.get();
                e.setName(name);
                e.setPhoneNumber(phone);
                agentRepo.save(e);
            } else {
                var e = new AgentEntity();
                e.setExternalId(tookanId);
                e.setName(name);
                e.setPhoneNumber(phone);
                agentRepo.save(e);
            }
            upserts++;
        }
        return upserts;
    }

    public int syncGeofences() {
        var rows = client.listGeofences();
        int upserts = 0;
        for (var r : rows) {
            String tookanId = String.valueOf(r.get("id"));
            String name = (String) r.get("name");
            String polygon = (String) r.get("polygon_geojson"); // or whatever field

            var existing = geofenceRepo.findByExternalId(tookanId);
            if (existing.isPresent()) {
                var e = existing.get();
                e.setName(name);
                e.setPolygonCoordinates(polygon);
                geofenceRepo.save(e);
            } else {
                var e = new GeofenceEntity();
                e.setExternalId(tookanId);
                e.setName(name);
                e.setPolygonCoordinates(polygon);
                geofenceRepo.save(e);
            }
            upserts++;
        }
        return upserts;
    }
}

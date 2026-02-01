package com.veylor.smp.teleport;

import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportRequestManager {
    private final Map<UUID, TeleportRequest> requests = new HashMap<>();
    private static final long REQUEST_TIMEOUT_SECONDS = 60;

    public void requestTeleport(Player requester, Player target) {
        requests.put(target.getUniqueId(), new TeleportRequest(requester.getUniqueId(), Instant.now()));
    }

    public UUID getRequester(UUID target) {
        TeleportRequest request = requests.get(target);
        if (request == null) {
            return null;
        }
        if (Instant.now().minusSeconds(REQUEST_TIMEOUT_SECONDS).isAfter(request.createdAt())) {
            requests.remove(target);
            return null;
        }
        return request.requester();
    }

    public void clear(UUID target) {
        requests.remove(target);
    }

    private record TeleportRequest(UUID requester, Instant createdAt) {
    }
}

package com.veylor.smp.minigame;

import java.util.UUID;

public record MinigameSession(MinigameType type, UUID opponent) {
}

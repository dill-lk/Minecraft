/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.worldselection;

import net.mayaan.client.gui.screens.worldselection.WorldCreationUiState;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.gamerules.GameRuleMap;
import net.mayaan.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import org.jspecify.annotations.Nullable;

public record InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode selectedGameMode, GameRuleMap gameRuleOverwrites, @Nullable ResourceKey<FlatLevelGeneratorPreset> flatLevelPreset) {
}


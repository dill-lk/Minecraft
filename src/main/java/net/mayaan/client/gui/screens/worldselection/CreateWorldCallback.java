/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.worldselection;

import java.nio.file.Path;
import java.util.Optional;
import net.mayaan.client.gui.screens.worldselection.CreateWorldScreen;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.server.RegistryLayer;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.storage.LevelDataAndDimensions;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface CreateWorldCallback {
    public boolean create(CreateWorldScreen var1, LayeredRegistryAccess<RegistryLayer> var2, LevelDataAndDimensions.WorldDataAndGenSettings var3, Optional<GameRules> var4, @Nullable Path var5);
}


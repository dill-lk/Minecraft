/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryLocalDifficulty
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity == null || serverChunk == null || !(serverOrClientLevel instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)serverOrClientLevel;
        BlockPos feetPos = entity.blockPosition();
        if (serverLevel.isInsideBuildHeight(feetPos.getY())) {
            float moonBrightness = serverLevel.getMoonBrightness(feetPos);
            long localTime = serverChunk.getInhabitedTime();
            DifficultyInstance localDifficulty = new DifficultyInstance(serverLevel.getDifficulty(), serverLevel.getOverworldClockTime(), localTime, moonBrightness);
            displayer.addLine(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f", Float.valueOf(localDifficulty.getEffectiveDifficulty()), Float.valueOf(localDifficulty.getSpecialMultiplier())));
        }
    }
}


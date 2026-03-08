/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.chunk;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.EmptyLevelChunk;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.chunk.LevelChunkSection;
import net.mayaan.world.level.chunk.PalettedContainer;
import net.mayaan.world.level.levelgen.DebugLevelSource;
import org.jspecify.annotations.Nullable;

class SectionCopy {
    private final Map<BlockPos, BlockEntity> blockEntities;
    private final @Nullable PalettedContainer<BlockState> section;
    private final boolean debug;
    private final LevelHeightAccessor levelHeightAccessor;

    SectionCopy(LevelChunk levelChunk, int sectionIndex) {
        this.levelHeightAccessor = levelChunk;
        this.debug = levelChunk.getLevel().isDebug();
        this.blockEntities = ImmutableMap.copyOf(levelChunk.getBlockEntities());
        if (levelChunk instanceof EmptyLevelChunk) {
            this.section = null;
        } else {
            LevelChunkSection levelChunkSection;
            LevelChunkSection[] sections = levelChunk.getSections();
            this.section = sectionIndex < 0 || sectionIndex >= sections.length ? null : ((levelChunkSection = sections[sectionIndex]).hasOnlyAir() ? null : levelChunkSection.getStates().copy());
        }
    }

    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return this.blockEntities.get(pos);
    }

    public BlockState getBlockState(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (this.debug) {
            BlockState blockState = null;
            if (y == 60) {
                blockState = Blocks.BARRIER.defaultBlockState();
            }
            if (y == 70) {
                blockState = DebugLevelSource.getBlockStateFor(x, z);
            }
            return blockState == null ? Blocks.AIR.defaultBlockState() : blockState;
        }
        if (this.section == null) {
            return Blocks.AIR.defaultBlockState();
        }
        try {
            return this.section.get(x & 0xF, y & 0xF, z & 0xF);
        }
        catch (Throwable t) {
            CrashReport report = CrashReport.forThrowable(t, "Getting block state");
            CrashReportCategory category = report.addCategory("Block being got");
            category.setDetail("Location", () -> CrashReportCategory.formatLocation(this.levelHeightAccessor, x, y, z));
            throw new ReportedException(report);
        }
    }
}


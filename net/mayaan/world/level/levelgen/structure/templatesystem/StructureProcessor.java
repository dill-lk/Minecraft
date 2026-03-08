/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

public abstract class StructureProcessor {
    public  @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
        return processedBlockInfo;
    }

    protected abstract StructureProcessorType<?> getType();

    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, List<StructureTemplate.StructureBlockInfo> originalBlockInfoList, List<StructureTemplate.StructureBlockInfo> processedBlockInfoList, StructurePlaceSettings settings) {
        return processedBlockInfoList;
    }
}


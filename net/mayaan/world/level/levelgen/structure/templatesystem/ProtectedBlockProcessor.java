/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.registries.Registries;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

public class ProtectedBlockProcessor
extends StructureProcessor {
    public final TagKey<Block> cannotReplace;
    public static final MapCodec<ProtectedBlockProcessor> CODEC = TagKey.hashedCodec(Registries.BLOCK).xmap(ProtectedBlockProcessor::new, e -> e.cannotReplace).fieldOf("value");

    public ProtectedBlockProcessor(TagKey<Block> cannotReplace) {
        this.cannotReplace = cannotReplace;
    }

    @Override
    public  @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
        if (Feature.isReplaceable(this.cannotReplace).test(level.getBlockState(processedBlockInfo.pos()))) {
            return processedBlockInfo;
        }
        return null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.PROTECTED_BLOCKS;
    }
}


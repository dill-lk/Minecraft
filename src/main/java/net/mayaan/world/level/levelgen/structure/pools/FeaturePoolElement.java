/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.structure.pools;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.FrontAndTop;
import net.mayaan.core.Holder;
import net.mayaan.core.Vec3i;
import net.mayaan.data.worldgen.Pools;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.resources.Identifier;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.JigsawBlock;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.JigsawBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class FeaturePoolElement
extends StructurePoolElement {
    public static final MapCodec<FeaturePoolElement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(e -> e.feature), FeaturePoolElement.projectionCodec()).apply((Applicative)i, FeaturePoolElement::new));
    private static final Identifier DEFAULT_JIGSAW_NAME = Identifier.withDefaultNamespace("bottom");
    private final Holder<PlacedFeature> feature;
    private final CompoundTag defaultJigsawNBT;

    protected FeaturePoolElement(Holder<PlacedFeature> feature, StructureTemplatePool.Projection projection) {
        super(projection);
        this.feature = feature;
        this.defaultJigsawNBT = this.fillDefaultJigsawNBT();
    }

    private CompoundTag fillDefaultJigsawNBT() {
        CompoundTag tag = new CompoundTag();
        tag.store("name", Identifier.CODEC, DEFAULT_JIGSAW_NAME);
        tag.putString("final_state", "minecraft:air");
        tag.store("pool", JigsawBlockEntity.POOL_CODEC, Pools.EMPTY);
        tag.store("target", Identifier.CODEC, JigsawBlockEntity.EMPTY_ID);
        tag.store("joint", JigsawBlockEntity.JointType.CODEC, JigsawBlockEntity.JointType.ROLLABLE);
        return tag;
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
        return Vec3i.ZERO;
    }

    @Override
    public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) {
        return List.of(StructureTemplate.JigsawBlockInfo.of(new StructureTemplate.StructureBlockInfo(position, (BlockState)Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT)));
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
        Vec3i size = this.getSize(structureTemplateManager, rotation);
        return new BoundingBox(position.getX(), position.getY(), position.getZ(), position.getX() + size.getX(), position.getY() + size.getY(), position.getZ() + size.getZ());
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
        return this.feature.value().place(level, generator, random, position);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.FEATURE;
    }

    public String toString() {
        return "Feature[" + String.valueOf(this.feature) + "]";
    }
}


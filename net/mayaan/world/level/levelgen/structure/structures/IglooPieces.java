/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.mayaan.world.level.levelgen.structure.structures;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.resources.Identifier;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.ChestBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.StructurePieceAccessor;
import net.mayaan.world.level.levelgen.structure.TemplateStructurePiece;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceType;
import net.mayaan.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.mayaan.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;

public class IglooPieces {
    public static final int GENERATION_HEIGHT = 90;
    private static final Identifier STRUCTURE_LOCATION_IGLOO = Identifier.withDefaultNamespace("igloo/top");
    private static final Identifier STRUCTURE_LOCATION_LADDER = Identifier.withDefaultNamespace("igloo/middle");
    private static final Identifier STRUCTURE_LOCATION_LABORATORY = Identifier.withDefaultNamespace("igloo/bottom");
    private static final Map<Identifier, BlockPos> PIVOTS = ImmutableMap.of((Object)STRUCTURE_LOCATION_IGLOO, (Object)new BlockPos(3, 5, 5), (Object)STRUCTURE_LOCATION_LADDER, (Object)new BlockPos(1, 3, 1), (Object)STRUCTURE_LOCATION_LABORATORY, (Object)new BlockPos(3, 6, 7));
    private static final Map<Identifier, BlockPos> OFFSETS = ImmutableMap.of((Object)STRUCTURE_LOCATION_IGLOO, (Object)BlockPos.ZERO, (Object)STRUCTURE_LOCATION_LADDER, (Object)new BlockPos(2, -3, 4), (Object)STRUCTURE_LOCATION_LABORATORY, (Object)new BlockPos(0, -3, -2));

    public static void addPieces(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
        if (random.nextDouble() < 0.5) {
            int depth = random.nextInt(8) + 4;
            structurePieceAccessor.addPiece(new IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_LABORATORY, position, rotation, depth * 3));
            for (int i = 0; i < depth - 1; ++i) {
                structurePieceAccessor.addPiece(new IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_LADDER, position, rotation, i * 3));
            }
        }
        structurePieceAccessor.addPiece(new IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_IGLOO, position, rotation, 0));
    }

    public static class IglooPiece
    extends TemplateStructurePiece {
        public IglooPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation, int depth) {
            super(StructurePieceType.IGLOO, 0, structureTemplateManager, templateLocation, templateLocation.toString(), IglooPiece.makeSettings(rotation, templateLocation), IglooPiece.makePosition(templateLocation, position, depth));
        }

        public IglooPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
            super(StructurePieceType.IGLOO, tag, structureTemplateManager, location -> IglooPiece.makeSettings(tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow(), location));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, Identifier templateLocation) {
            return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(PIVOTS.get(templateLocation)).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
        }

        private static BlockPos makePosition(Identifier templateLocation, BlockPos position, int depth) {
            return position.offset(OFFSETS.get(templateLocation)).below(depth);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
        }

        @Override
        protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
            if (!"chest".equals(markerId)) {
                return;
            }
            level.setBlock(position, Blocks.AIR.defaultBlockState(), 3);
            BlockEntity chest = level.getBlockEntity(position.below());
            if (chest instanceof ChestBlockEntity) {
                ((ChestBlockEntity)chest).setLootTable(BuiltInLootTables.IGLOO_CHEST, random.nextLong());
            }
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
            BlockPos trapDoorPos;
            BlockState belowState;
            Identifier templateLocation = Identifier.parse(this.templateName);
            StructurePlaceSettings settings = IglooPiece.makeSettings(this.placeSettings.getRotation(), templateLocation);
            BlockPos offset = OFFSETS.get(templateLocation);
            BlockPos entrancePos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3 - offset.getX(), 0, -offset.getZ())));
            int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, entrancePos.getX(), entrancePos.getZ());
            BlockPos oldTemplatePos = this.templatePosition;
            this.templatePosition = this.templatePosition.offset(0, height - 90 - 1, 0);
            super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
            if (templateLocation.equals(STRUCTURE_LOCATION_IGLOO) && !(belowState = level.getBlockState((trapDoorPos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3, 0, 5)))).below())).isAir() && !belowState.is(Blocks.LADDER)) {
                level.setBlock(trapDoorPos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
            }
            this.templatePosition = oldTemplatePos;
        }
    }
}


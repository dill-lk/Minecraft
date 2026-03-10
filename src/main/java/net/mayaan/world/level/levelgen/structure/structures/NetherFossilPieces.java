/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure.structures;

import net.mayaan.core.BlockPos;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.resources.Identifier;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.StructurePieceAccessor;
import net.mayaan.world.level.levelgen.structure.TemplateStructurePiece;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceType;
import net.mayaan.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class NetherFossilPieces {
    private static final Identifier[] FOSSILS = new Identifier[]{Identifier.withDefaultNamespace("nether_fossils/fossil_1"), Identifier.withDefaultNamespace("nether_fossils/fossil_2"), Identifier.withDefaultNamespace("nether_fossils/fossil_3"), Identifier.withDefaultNamespace("nether_fossils/fossil_4"), Identifier.withDefaultNamespace("nether_fossils/fossil_5"), Identifier.withDefaultNamespace("nether_fossils/fossil_6"), Identifier.withDefaultNamespace("nether_fossils/fossil_7"), Identifier.withDefaultNamespace("nether_fossils/fossil_8"), Identifier.withDefaultNamespace("nether_fossils/fossil_9"), Identifier.withDefaultNamespace("nether_fossils/fossil_10"), Identifier.withDefaultNamespace("nether_fossils/fossil_11"), Identifier.withDefaultNamespace("nether_fossils/fossil_12"), Identifier.withDefaultNamespace("nether_fossils/fossil_13"), Identifier.withDefaultNamespace("nether_fossils/fossil_14")};

    public static void addPieces(StructureTemplateManager structureTemplateManager, StructurePieceAccessor structurePieceAccessor, RandomSource random, BlockPos position) {
        Rotation nextRotation = Rotation.getRandom(random);
        structurePieceAccessor.addPiece(new NetherFossilPiece(structureTemplateManager, Util.getRandom(FOSSILS, random), position, nextRotation));
    }

    public static class NetherFossilPiece
    extends TemplateStructurePiece {
        public NetherFossilPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation) {
            super(StructurePieceType.NETHER_FOSSIL, 0, structureTemplateManager, templateLocation, templateLocation.toString(), NetherFossilPiece.makeSettings(rotation), position);
        }

        public NetherFossilPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
            super(StructurePieceType.NETHER_FOSSIL, tag, structureTemplateManager, (Identifier location) -> NetherFossilPiece.makeSettings(tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
        }

        @Override
        protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
            BoundingBox fossilBB = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
            chunkBB.encapsulate(fossilBB);
            super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
            this.placeDriedGhast(level, random, fossilBB, chunkBB);
        }

        private void placeDriedGhast(WorldGenLevel level, RandomSource random, BoundingBox fossilBB, BoundingBox chunkBB) {
            int z;
            int y;
            int x;
            BlockPos randomPos;
            RandomSource positionalRandom = RandomSource.createThreadLocalInstance(level.getSeed()).forkPositional().at(fossilBB.getCenter());
            if (positionalRandom.nextFloat() < 0.5f && level.getBlockState(randomPos = new BlockPos(x = fossilBB.minX() + positionalRandom.nextInt(fossilBB.getXSpan()), y = fossilBB.minY(), z = fossilBB.minZ() + positionalRandom.nextInt(fossilBB.getZSpan()))).isAir() && chunkBB.isInside(randomPos)) {
                level.setBlock(randomPos, Blocks.DRIED_GHAST.defaultBlockState().rotate(Rotation.getRandom(positionalRandom)), 2);
            }
        }
    }
}


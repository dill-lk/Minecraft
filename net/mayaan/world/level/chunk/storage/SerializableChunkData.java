/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DynamicOps
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  it.unimi.dsi.fastutil.shorts.ShortArrayList
 *  it.unimi.dsi.fastutil.shorts.ShortList
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.world.level.chunk.storage;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.mayaan.Optionull;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.SectionPos;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.ListTag;
import net.mayaan.nbt.LongArrayTag;
import net.mayaan.nbt.NbtException;
import net.mayaan.nbt.NbtOps;
import net.mayaan.nbt.NbtUtils;
import net.mayaan.nbt.ShortTag;
import net.mayaan.nbt.Tag;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerChunkCache;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ThreadedLevelLightEngine;
import net.mayaan.util.ProblemReporter;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ai.village.poi.PoiManager;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.LightLayer;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.CarvingMask;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.chunk.ChunkSource;
import net.mayaan.world.level.chunk.DataLayer;
import net.mayaan.world.level.chunk.ImposterProtoChunk;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.chunk.LevelChunkSection;
import net.mayaan.world.level.chunk.PalettedContainer;
import net.mayaan.world.level.chunk.PalettedContainerFactory;
import net.mayaan.world.level.chunk.PalettedContainerRO;
import net.mayaan.world.level.chunk.ProtoChunk;
import net.mayaan.world.level.chunk.UpgradeData;
import net.mayaan.world.level.chunk.status.ChunkStatus;
import net.mayaan.world.level.chunk.status.ChunkType;
import net.mayaan.world.level.chunk.storage.RegionStorageInfo;
import net.mayaan.world.level.levelgen.BelowZeroRetrogen;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.blending.BlendingData;
import net.mayaan.world.level.levelgen.structure.Structure;
import net.mayaan.world.level.levelgen.structure.StructureStart;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.mayaan.world.level.lighting.LevelLightEngine;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.storage.TagValueInput;
import net.mayaan.world.ticks.LevelChunkTicks;
import net.mayaan.world.ticks.ProtoChunkTicks;
import net.mayaan.world.ticks.SavedTick;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public record SerializableChunkData(PalettedContainerFactory containerFactory, ChunkPos chunkPos, int minSectionY, long lastUpdateTime, long inhabitedTime, ChunkStatus chunkStatus, @Nullable BlendingData.Packed blendingData, @Nullable BelowZeroRetrogen belowZeroRetrogen, UpgradeData upgradeData, long @Nullable [] carvingMask, Map<Heightmap.Types, long[]> heightmaps, ChunkAccess.PackedTicks packedTicks, @Nullable ShortList[] postProcessingSections, boolean lightCorrect, List<SectionData> sectionData, List<CompoundTag> entities, List<CompoundTag> blockEntities, CompoundTag structureData) {
    private static final Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.BLOCK.byNameCodec()).listOf();
    private static final Codec<List<SavedTick<Fluid>>> FLUID_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.FLUID.byNameCodec()).listOf();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String TAG_UPGRADE_DATA = "UpgradeData";
    private static final String BLOCK_TICKS_TAG = "block_ticks";
    private static final String FLUID_TICKS_TAG = "fluid_ticks";
    public static final String X_POS_TAG = "xPos";
    public static final String Z_POS_TAG = "zPos";
    public static final String HEIGHTMAPS_TAG = "Heightmaps";
    public static final String IS_LIGHT_ON_TAG = "isLightOn";
    public static final String SECTIONS_TAG = "sections";
    public static final String BLOCK_LIGHT_TAG = "BlockLight";
    public static final String SKY_LIGHT_TAG = "SkyLight";

    public static @Nullable SerializableChunkData parse(LevelHeightAccessor levelHeight, PalettedContainerFactory containerFactory, CompoundTag chunkData) {
        if (chunkData.getString("Status").isEmpty()) {
            return null;
        }
        ChunkPos chunkPos = new ChunkPos(chunkData.getIntOr(X_POS_TAG, 0), chunkData.getIntOr(Z_POS_TAG, 0));
        long lastUpdateTime = chunkData.getLongOr("LastUpdate", 0L);
        long inhabitedTime = chunkData.getLongOr("InhabitedTime", 0L);
        ChunkStatus status = chunkData.read("Status", ChunkStatus.CODEC).orElse(ChunkStatus.EMPTY);
        UpgradeData upgradeData = chunkData.getCompound(TAG_UPGRADE_DATA).map(tag -> new UpgradeData((CompoundTag)tag, levelHeight)).orElse(UpgradeData.EMPTY);
        boolean lightCorrect = chunkData.getBooleanOr(IS_LIGHT_ON_TAG, false);
        BlendingData.Packed blendingData = chunkData.read("blending_data", BlendingData.Packed.CODEC).orElse(null);
        BelowZeroRetrogen belowZeroRetrogen = chunkData.read("below_zero_retrogen", BelowZeroRetrogen.CODEC).orElse(null);
        long[] carvingMask = chunkData.getLongArray("carving_mask").orElse(null);
        EnumMap<Heightmap.Types, long[]> heightmaps = new EnumMap<Heightmap.Types, long[]>(Heightmap.Types.class);
        chunkData.getCompound(HEIGHTMAPS_TAG).ifPresent(heightmapsTag -> {
            for (Heightmap.Types type : status.heightmapsAfter()) {
                heightmapsTag.getLongArray(type.getSerializationKey()).ifPresent(longs -> heightmaps.put(type, (long[])longs));
            }
        });
        List<SavedTick<Block>> blockTicks = SavedTick.filterTickListForChunk(chunkData.read(BLOCK_TICKS_TAG, BLOCK_TICKS_CODEC).orElse(List.of()), chunkPos);
        List<SavedTick<Fluid>> fluidTicks = SavedTick.filterTickListForChunk(chunkData.read(FLUID_TICKS_TAG, FLUID_TICKS_CODEC).orElse(List.of()), chunkPos);
        ChunkAccess.PackedTicks packedTicks = new ChunkAccess.PackedTicks(blockTicks, fluidTicks);
        ListTag postProcessTags = chunkData.getListOrEmpty("PostProcessing");
        @Nullable ShortList[] postProcessingSections = new ShortList[postProcessTags.size()];
        for (int sectionIndex = 0; sectionIndex < postProcessTags.size(); ++sectionIndex) {
            ListTag offsetsTag = postProcessTags.getList(sectionIndex).orElse(null);
            if (offsetsTag == null || offsetsTag.isEmpty()) continue;
            ShortArrayList packedOffsets = new ShortArrayList(offsetsTag.size());
            for (int i = 0; i < offsetsTag.size(); ++i) {
                packedOffsets.add(offsetsTag.getShortOr(i, (short)0));
            }
            postProcessingSections[sectionIndex] = packedOffsets;
        }
        List<CompoundTag> entities = chunkData.getList("entities").stream().flatMap(ListTag::compoundStream).toList();
        List<CompoundTag> blockEntities = chunkData.getList("block_entities").stream().flatMap(ListTag::compoundStream).toList();
        CompoundTag structureData = chunkData.getCompoundOrEmpty("structures");
        ListTag sectionTags = chunkData.getListOrEmpty(SECTIONS_TAG);
        ArrayList<SectionData> sectionData = new ArrayList<SectionData>(sectionTags.size());
        Codec<PalettedContainerRO<Holder<Biome>>> biomesCodec = containerFactory.biomeContainerCodec();
        Codec<PalettedContainer<BlockState>> blockStatesCodec = containerFactory.blockStatesContainerCodec();
        for (int i = 0; i < sectionTags.size(); ++i) {
            LevelChunkSection section;
            Optional<CompoundTag> maybeSectionTag = sectionTags.getCompound(i);
            if (maybeSectionTag.isEmpty()) continue;
            CompoundTag sectionTag = maybeSectionTag.get();
            byte y = sectionTag.getByteOr("Y", (byte)0);
            if (y >= levelHeight.getMinSectionY() && y <= levelHeight.getMaxSectionY()) {
                PalettedContainer blocks = sectionTag.getCompound("block_states").map(container -> (PalettedContainer)blockStatesCodec.parse((DynamicOps)NbtOps.INSTANCE, container).promotePartial(msg -> SerializableChunkData.logErrors(chunkPos, y, msg)).getOrThrow(ChunkReadException::new)).orElseGet(containerFactory::createForBlockStates);
                PalettedContainerRO biomes = sectionTag.getCompound("biomes").map(container -> (PalettedContainerRO)biomesCodec.parse((DynamicOps)NbtOps.INSTANCE, container).promotePartial(msg -> SerializableChunkData.logErrors(chunkPos, y, msg)).getOrThrow(ChunkReadException::new)).orElseGet(containerFactory::createForBiomes);
                section = new LevelChunkSection(blocks, biomes);
            } else {
                section = null;
            }
            DataLayer blockLight = sectionTag.getByteArray(BLOCK_LIGHT_TAG).map(DataLayer::new).orElse(null);
            DataLayer skyLight = sectionTag.getByteArray(SKY_LIGHT_TAG).map(DataLayer::new).orElse(null);
            sectionData.add(new SectionData(y, section, blockLight, skyLight));
        }
        return new SerializableChunkData(containerFactory, chunkPos, levelHeight.getMinSectionY(), lastUpdateTime, inhabitedTime, status, blendingData, belowZeroRetrogen, upgradeData, carvingMask, heightmaps, packedTicks, postProcessingSections, lightCorrect, sectionData, entities, blockEntities, structureData);
    }

    public ProtoChunk read(ServerLevel level, PoiManager poiManager, RegionStorageInfo regionInfo, ChunkPos pos) {
        ChunkAccess chunk;
        if (!Objects.equals(pos, this.chunkPos)) {
            LOGGER.error("Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", new Object[]{pos, pos, this.chunkPos});
            level.getServer().reportMisplacedChunk(this.chunkPos, pos, regionInfo);
        }
        int sectionCount = level.getSectionsCount();
        LevelChunkSection[] sections = new LevelChunkSection[sectionCount];
        boolean skyLight = level.dimensionType().hasSkyLight();
        ServerChunkCache chunkSource = level.getChunkSource();
        LevelLightEngine lightEngine = ((ChunkSource)chunkSource).getLightEngine();
        PalettedContainerFactory containerFactory = level.palettedContainerFactory();
        boolean loadedAnyLight = false;
        for (SectionData section : this.sectionData) {
            boolean hasSkyLight;
            SectionPos sectionPos = SectionPos.of(pos, section.y);
            if (section.chunkSection != null) {
                sections[level.getSectionIndexFromSectionY((int)section.y)] = section.chunkSection;
                poiManager.checkConsistencyWithBlocks(sectionPos, section.chunkSection);
            }
            boolean hasBlockLight = section.blockLight != null;
            boolean bl = hasSkyLight = skyLight && section.skyLight != null;
            if (!hasBlockLight && !hasSkyLight) continue;
            if (!loadedAnyLight) {
                lightEngine.retainData(pos, true);
                loadedAnyLight = true;
            }
            if (hasBlockLight) {
                lightEngine.queueSectionData(LightLayer.BLOCK, sectionPos, section.blockLight);
            }
            if (!hasSkyLight) continue;
            lightEngine.queueSectionData(LightLayer.SKY, sectionPos, section.skyLight);
        }
        ChunkType chunkType = this.chunkStatus.getChunkType();
        if (chunkType == ChunkType.LEVELCHUNK) {
            blockTicks = new LevelChunkTicks(this.packedTicks.blocks());
            fluidTicks = new LevelChunkTicks(this.packedTicks.fluids());
            chunk = new LevelChunk(level.getLevel(), pos, this.upgradeData, (LevelChunkTicks<Block>)((Object)blockTicks), (LevelChunkTicks<Fluid>)fluidTicks, this.inhabitedTime, sections, SerializableChunkData.postLoadChunk(level, this.entities, this.blockEntities), BlendingData.unpack(this.blendingData));
        } else {
            blockTicks = ProtoChunkTicks.load(this.packedTicks.blocks());
            fluidTicks = ProtoChunkTicks.load(this.packedTicks.fluids());
            ProtoChunk protoChunk = new ProtoChunk(pos, this.upgradeData, sections, blockTicks, (ProtoChunkTicks<Fluid>)fluidTicks, level, containerFactory, BlendingData.unpack(this.blendingData));
            chunk = protoChunk;
            chunk.setInhabitedTime(this.inhabitedTime);
            if (this.belowZeroRetrogen != null) {
                protoChunk.setBelowZeroRetrogen(this.belowZeroRetrogen);
            }
            protoChunk.setPersistedStatus(this.chunkStatus);
            if (this.chunkStatus.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
                protoChunk.setLightEngine(lightEngine);
            }
        }
        chunk.setLightCorrect(this.lightCorrect);
        EnumSet<Heightmap.Types> toPrime = EnumSet.noneOf(Heightmap.Types.class);
        for (Heightmap.Types type : chunk.getPersistedStatus().heightmapsAfter()) {
            long[] heightmap = this.heightmaps.get(type);
            if (heightmap != null) {
                chunk.setHeightmap(type, heightmap);
                continue;
            }
            toPrime.add(type);
        }
        Heightmap.primeHeightmaps(chunk, toPrime);
        chunk.setAllStarts(SerializableChunkData.unpackStructureStart(StructurePieceSerializationContext.fromLevel(level), this.structureData, level.getSeed()));
        chunk.setAllReferences(SerializableChunkData.unpackStructureReferences(level.registryAccess(), pos, this.structureData));
        for (int sectionIndex = 0; sectionIndex < this.postProcessingSections.length; ++sectionIndex) {
            ShortList postProcessingSection = this.postProcessingSections[sectionIndex];
            if (postProcessingSection == null) continue;
            chunk.addPackedPostProcess(postProcessingSection, sectionIndex);
        }
        if (chunkType == ChunkType.LEVELCHUNK) {
            return new ImposterProtoChunk((LevelChunk)chunk, false);
        }
        ProtoChunk protoChunk = (ProtoChunk)chunk;
        for (CompoundTag entity : this.entities) {
            protoChunk.addEntity(entity);
        }
        for (CompoundTag blockEntity : this.blockEntities) {
            protoChunk.setBlockEntityNbt(blockEntity);
        }
        if (this.carvingMask != null) {
            protoChunk.setCarvingMask(new CarvingMask(this.carvingMask, chunk.getMinY()));
        }
        return protoChunk;
    }

    private static void logErrors(ChunkPos pos, int sectionY, String message) {
        LOGGER.error("Recoverable errors when loading section [{}, {}, {}]: {}", new Object[]{pos.x(), sectionY, pos.z(), message});
    }

    public static SerializableChunkData copyOf(ServerLevel level, ChunkAccess chunk) {
        if (!chunk.canBeSerialized()) {
            throw new IllegalArgumentException("Chunk can't be serialized: " + String.valueOf(chunk));
        }
        ChunkPos pos = chunk.getPos();
        ArrayList<SectionData> sectionData = new ArrayList<SectionData>();
        LevelChunkSection[] chunkSections = chunk.getSections();
        ThreadedLevelLightEngine lightEngine = level.getChunkSource().getLightEngine();
        for (int sectionY = lightEngine.getMinLightSection(); sectionY < lightEngine.getMaxLightSection(); ++sectionY) {
            DataLayer skyLight;
            int sectionIndex = chunk.getSectionIndexFromSectionY(sectionY);
            boolean hasSection = sectionIndex >= 0 && sectionIndex < chunkSections.length;
            DataLayer sourceBlockLight = lightEngine.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(pos, sectionY));
            DataLayer sourceSkyLight = lightEngine.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(pos, sectionY));
            DataLayer dataLayer = sourceBlockLight != null && !sourceBlockLight.isEmpty() ? sourceBlockLight.copy() : null;
            DataLayer dataLayer2 = skyLight = sourceSkyLight != null && !sourceSkyLight.isEmpty() ? sourceSkyLight.copy() : null;
            if (!hasSection && dataLayer == null && skyLight == null) continue;
            LevelChunkSection section = hasSection ? chunkSections[sectionIndex].copy() : null;
            sectionData.add(new SectionData(sectionY, section, dataLayer, skyLight));
        }
        ArrayList<CompoundTag> blockEntities = new ArrayList<CompoundTag>(chunk.getBlockEntitiesPos().size());
        for (BlockPos blockPos : chunk.getBlockEntitiesPos()) {
            CompoundTag blockEntityTag = chunk.getBlockEntityNbtForSaving(blockPos, level.registryAccess());
            if (blockEntityTag == null) continue;
            blockEntities.add(blockEntityTag);
        }
        ArrayList<CompoundTag> entities = new ArrayList<CompoundTag>();
        long[] carvingMask = null;
        if (chunk.getPersistedStatus().getChunkType() == ChunkType.PROTOCHUNK) {
            ProtoChunk protoChunk = (ProtoChunk)chunk;
            entities.addAll(protoChunk.getEntities());
            CarvingMask existingMask = protoChunk.getCarvingMask();
            if (existingMask != null) {
                carvingMask = existingMask.toArray();
            }
        }
        EnumMap<Heightmap.Types, long[]> heightmaps = new EnumMap<Heightmap.Types, long[]>(Heightmap.Types.class);
        for (Map.Entry entry : chunk.getHeightmaps()) {
            if (!chunk.getPersistedStatus().heightmapsAfter().contains(entry.getKey())) continue;
            long[] data = ((Heightmap)entry.getValue()).getRawData();
            heightmaps.put((Heightmap.Types)entry.getKey(), (long[])data.clone());
        }
        ChunkAccess.PackedTicks ticksForSerialization = chunk.getTicksForSerialization(level.getGameTime());
        @Nullable ShortList[] shortListArray = (ShortList[])Arrays.stream(chunk.getPostProcessing()).map(shorts -> shorts != null && !shorts.isEmpty() ? new ShortArrayList(shorts) : null).toArray(ShortList[]::new);
        CompoundTag structureData = SerializableChunkData.packStructureData(StructurePieceSerializationContext.fromLevel(level), pos, chunk.getAllStarts(), chunk.getAllReferences());
        return new SerializableChunkData(level.palettedContainerFactory(), pos, chunk.getMinSectionY(), level.getGameTime(), chunk.getInhabitedTime(), chunk.getPersistedStatus(), Optionull.map(chunk.getBlendingData(), BlendingData::pack), chunk.getBelowZeroRetrogen(), chunk.getUpgradeData().copy(), carvingMask, heightmaps, ticksForSerialization, shortListArray, chunk.isLightCorrect(), sectionData, entities, blockEntities, structureData);
    }

    public CompoundTag write() {
        CompoundTag tag = NbtUtils.addCurrentDataVersion(new CompoundTag());
        tag.putInt(X_POS_TAG, this.chunkPos.x());
        tag.putInt("yPos", this.minSectionY);
        tag.putInt(Z_POS_TAG, this.chunkPos.z());
        tag.putLong("LastUpdate", this.lastUpdateTime);
        tag.putLong("InhabitedTime", this.inhabitedTime);
        tag.putString("Status", BuiltInRegistries.CHUNK_STATUS.getKey(this.chunkStatus).toString());
        tag.storeNullable("blending_data", BlendingData.Packed.CODEC, this.blendingData);
        tag.storeNullable("below_zero_retrogen", BelowZeroRetrogen.CODEC, this.belowZeroRetrogen);
        if (!this.upgradeData.isEmpty()) {
            tag.put(TAG_UPGRADE_DATA, this.upgradeData.write());
        }
        ListTag sectionTags = new ListTag();
        Codec<PalettedContainer<BlockState>> blockStatesCodec = this.containerFactory.blockStatesContainerCodec();
        Codec<PalettedContainerRO<Holder<Biome>>> biomeCodec = this.containerFactory.biomeContainerCodec();
        for (SectionData section : this.sectionData) {
            CompoundTag sectionTag = new CompoundTag();
            LevelChunkSection chunkSection = section.chunkSection;
            if (chunkSection != null) {
                sectionTag.store("block_states", blockStatesCodec, chunkSection.getStates());
                sectionTag.store("biomes", biomeCodec, chunkSection.getBiomes());
            }
            if (section.blockLight != null) {
                sectionTag.putByteArray(BLOCK_LIGHT_TAG, section.blockLight.getData());
            }
            if (section.skyLight != null) {
                sectionTag.putByteArray(SKY_LIGHT_TAG, section.skyLight.getData());
            }
            if (sectionTag.isEmpty()) continue;
            sectionTag.putByte("Y", (byte)section.y);
            sectionTags.add(sectionTag);
        }
        tag.put(SECTIONS_TAG, sectionTags);
        if (this.lightCorrect) {
            tag.putBoolean(IS_LIGHT_ON_TAG, true);
        }
        ListTag blockEntityTags = new ListTag();
        blockEntityTags.addAll(this.blockEntities);
        tag.put("block_entities", blockEntityTags);
        if (this.chunkStatus.getChunkType() == ChunkType.PROTOCHUNK) {
            ListTag entityTags = new ListTag();
            entityTags.addAll(this.entities);
            tag.put("entities", entityTags);
            if (this.carvingMask != null) {
                tag.putLongArray("carving_mask", this.carvingMask);
            }
        }
        SerializableChunkData.saveTicks(tag, this.packedTicks);
        tag.put("PostProcessing", SerializableChunkData.packOffsets(this.postProcessingSections));
        CompoundTag heightmapsTag = new CompoundTag();
        this.heightmaps.forEach((type, data) -> heightmapsTag.put(type.getSerializationKey(), new LongArrayTag((long[])data)));
        tag.put(HEIGHTMAPS_TAG, heightmapsTag);
        tag.put("structures", this.structureData);
        return tag;
    }

    private static void saveTicks(CompoundTag levelData, ChunkAccess.PackedTicks ticksForSerialization) {
        levelData.store(BLOCK_TICKS_TAG, BLOCK_TICKS_CODEC, ticksForSerialization.blocks());
        levelData.store(FLUID_TICKS_TAG, FLUID_TICKS_CODEC, ticksForSerialization.fluids());
    }

    public static ChunkStatus getChunkStatusFromTag(@Nullable CompoundTag tag) {
        return tag != null ? tag.read("Status", ChunkStatus.CODEC).orElse(ChunkStatus.EMPTY) : ChunkStatus.EMPTY;
    }

    private static @Nullable LevelChunk.PostLoadProcessor postLoadChunk(ServerLevel level, List<CompoundTag> entities, List<CompoundTag> blockEntities) {
        if (entities.isEmpty() && blockEntities.isEmpty()) {
            return null;
        }
        return levelChunk -> {
            if (!entities.isEmpty()) {
                try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(levelChunk.problemPath(), LOGGER);){
                    level.addLegacyChunkEntities(EntityType.loadEntitiesRecursive(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)level.registryAccess(), entities), level, EntitySpawnReason.LOAD));
                }
            }
            for (CompoundTag entityTag : blockEntities) {
                boolean keepPacked = entityTag.getBooleanOr("keepPacked", false);
                if (keepPacked) {
                    levelChunk.setBlockEntityNbt(entityTag);
                    continue;
                }
                BlockPos pos = BlockEntity.getPosFromTag(levelChunk.getPos(), entityTag);
                BlockEntity blockEntity = BlockEntity.loadStatic(pos, levelChunk.getBlockState(pos), entityTag, level.registryAccess());
                if (blockEntity == null) continue;
                levelChunk.setBlockEntity(blockEntity);
            }
        };
    }

    private static CompoundTag packStructureData(StructurePieceSerializationContext context, ChunkPos pos, Map<Structure, StructureStart> starts, Map<Structure, LongSet> references) {
        CompoundTag outTag = new CompoundTag();
        CompoundTag startsTag = new CompoundTag();
        HolderLookup.RegistryLookup structuresRegistry = context.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        for (Map.Entry<Structure, StructureStart> entry : starts.entrySet()) {
            Identifier key = structuresRegistry.getKey(entry.getKey());
            startsTag.put(key.toString(), entry.getValue().createTag(context, pos));
        }
        outTag.put("starts", startsTag);
        CompoundTag referencesTag = new CompoundTag();
        for (Map.Entry<Structure, LongSet> entry : references.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            Identifier key = structuresRegistry.getKey(entry.getKey());
            referencesTag.putLongArray(key.toString(), entry.getValue().toLongArray());
        }
        outTag.put("References", referencesTag);
        return outTag;
    }

    private static Map<Structure, StructureStart> unpackStructureStart(StructurePieceSerializationContext context, CompoundTag tag, long seed) {
        HashMap outmap = Maps.newHashMap();
        HolderLookup.RegistryLookup structuresRegistry = context.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        CompoundTag startsTag = tag.getCompoundOrEmpty("starts");
        for (String key : startsTag.keySet()) {
            Identifier id = Identifier.tryParse(key);
            Structure startFeature = (Structure)structuresRegistry.getValue(id);
            if (startFeature == null) {
                LOGGER.error("Unknown structure start: {}", (Object)id);
                continue;
            }
            StructureStart start = StructureStart.loadStaticStart(context, startsTag.getCompoundOrEmpty(key), seed);
            if (start == null) continue;
            outmap.put(startFeature, start);
        }
        return outmap;
    }

    private static Map<Structure, LongSet> unpackStructureReferences(RegistryAccess registryAccess, ChunkPos pos, CompoundTag tag) {
        HashMap outmap = Maps.newHashMap();
        HolderLookup.RegistryLookup structuresRegistry = registryAccess.lookupOrThrow(Registries.STRUCTURE);
        CompoundTag referencesTag = tag.getCompoundOrEmpty("References");
        referencesTag.forEach((arg_0, arg_1) -> SerializableChunkData.lambda$unpackStructureReferences$0((Registry)structuresRegistry, pos, outmap, arg_0, arg_1));
        return outmap;
    }

    private static ListTag packOffsets(@Nullable ShortList[] sections) {
        ListTag listTag = new ListTag();
        for (ShortList offsetList : sections) {
            ListTag offsetsTag = new ListTag();
            if (offsetList != null) {
                for (int i = 0; i < offsetList.size(); ++i) {
                    offsetsTag.add(ShortTag.valueOf(offsetList.getShort(i)));
                }
            }
            listTag.add(offsetsTag);
        }
        return listTag;
    }

    private static /* synthetic */ void lambda$unpackStructureReferences$0(Registry structuresRegistry, ChunkPos pos, Map outmap, String key, Tag entry) {
        Identifier structureId = Identifier.tryParse(key);
        Structure structureType = (Structure)structuresRegistry.getValue(structureId);
        if (structureType == null) {
            LOGGER.warn("Found reference to unknown structure '{}' in chunk {}, discarding", (Object)structureId, (Object)pos);
            return;
        }
        Optional<long[]> longArray = entry.asLongArray();
        if (longArray.isEmpty()) {
            return;
        }
        outmap.put(structureType, new LongOpenHashSet(Arrays.stream(longArray.get()).filter(chunkLongPos -> {
            ChunkPos refPos = ChunkPos.unpack(chunkLongPos);
            if (refPos.getChessboardDistance(pos) > 8) {
                LOGGER.warn("Found invalid structure reference [ {} @ {} ] for chunk {}.", new Object[]{structureId, refPos, pos});
                return false;
            }
            return true;
        }).toArray()));
    }

    public record SectionData(int y, @Nullable LevelChunkSection chunkSection, @Nullable DataLayer blockLight, @Nullable DataLayer skyLight) {
    }

    public static class ChunkReadException
    extends NbtException {
        public ChunkReadException(String message) {
            super(message);
        }
    }
}


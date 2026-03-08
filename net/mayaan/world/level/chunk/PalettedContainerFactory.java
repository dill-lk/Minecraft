/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.chunk;

import com.mojang.serialization.Codec;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.Biomes;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.PalettedContainer;
import net.mayaan.world.level.chunk.PalettedContainerRO;
import net.mayaan.world.level.chunk.Strategy;

public record PalettedContainerFactory(Strategy<BlockState> blockStatesStrategy, BlockState defaultBlockState, Codec<PalettedContainer<BlockState>> blockStatesContainerCodec, Strategy<Holder<Biome>> biomeStrategy, Holder<Biome> defaultBiome, Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec) {
    public static PalettedContainerFactory create(RegistryAccess registries) {
        Strategy<BlockState> blockStateStrategy = Strategy.createForBlockStates(Block.BLOCK_STATE_REGISTRY);
        BlockState defaultBlockState = Blocks.AIR.defaultBlockState();
        HolderLookup.RegistryLookup biomes = registries.lookupOrThrow(Registries.BIOME);
        Strategy<Holder<Biome>> biomeStrategy = Strategy.createForBiomes(biomes.asHolderIdMap());
        Holder.Reference defaultBiome = biomes.getOrThrow(Biomes.PLAINS);
        return new PalettedContainerFactory(blockStateStrategy, defaultBlockState, PalettedContainer.codecRW(BlockState.CODEC, blockStateStrategy, defaultBlockState), biomeStrategy, defaultBiome, PalettedContainer.codecRO(biomes.holderByNameCodec(), biomeStrategy, defaultBiome));
    }

    public PalettedContainer<BlockState> createForBlockStates() {
        return new PalettedContainer<BlockState>(this.defaultBlockState, this.blockStatesStrategy);
    }

    public PalettedContainer<Holder<Biome>> createForBiomes() {
        return new PalettedContainer<Holder<Biome>>(this.defaultBiome, this.biomeStrategy);
    }
}


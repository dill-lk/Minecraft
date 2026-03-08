/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import net.mayaan.advancements.criterion.DataComponentMatchers;
import net.mayaan.advancements.criterion.NbtPredicate;
import net.mayaan.advancements.criterion.StatePropertiesPredicate;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.Registries;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.pattern.BlockInWorld;
import org.jspecify.annotations.Nullable;

public record BlockPredicate(Optional<HolderSet<Block>> blocks, Optional<StatePropertiesPredicate> properties, Optional<NbtPredicate> nbt, DataComponentMatchers components) {
    public static final Codec<BlockPredicate> CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("blocks").forGetter(BlockPredicate::blocks), (App)StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(BlockPredicate::properties), (App)NbtPredicate.CODEC.optionalFieldOf("nbt").forGetter(BlockPredicate::nbt), (App)DataComponentMatchers.CODEC.forGetter(BlockPredicate::components)).apply((Applicative)i, BlockPredicate::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockPredicate> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.BLOCK)), BlockPredicate::blocks, ByteBufCodecs.optional(StatePropertiesPredicate.STREAM_CODEC), BlockPredicate::properties, ByteBufCodecs.optional(NbtPredicate.STREAM_CODEC), BlockPredicate::nbt, DataComponentMatchers.STREAM_CODEC, BlockPredicate::components, BlockPredicate::new);

    public boolean matches(ServerLevel level, BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }
        if (!this.matchesState(level.getBlockState(pos))) {
            return false;
        }
        if (this.nbt.isPresent() || !this.components.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (this.nbt.isPresent() && !BlockPredicate.matchesBlockEntity(level, blockEntity, this.nbt.get())) {
                return false;
            }
            if (!this.components.isEmpty() && !BlockPredicate.matchesComponents(blockEntity, this.components)) {
                return false;
            }
        }
        return true;
    }

    public boolean matches(BlockInWorld blockInWorld) {
        if (!this.matchesState(blockInWorld.getState())) {
            return false;
        }
        return !this.nbt.isPresent() || BlockPredicate.matchesBlockEntity(blockInWorld.getLevel(), blockInWorld.getEntity(), this.nbt.get());
    }

    private boolean matchesState(BlockState state) {
        if (this.blocks.isPresent() && !state.is(this.blocks.get())) {
            return false;
        }
        return !this.properties.isPresent() || this.properties.get().matches(state);
    }

    private static boolean matchesBlockEntity(LevelReader level, @Nullable BlockEntity entity, NbtPredicate nbt) {
        return entity != null && nbt.matches(entity.saveWithFullMetadata(level.registryAccess()));
    }

    private static boolean matchesComponents(@Nullable BlockEntity entity, DataComponentMatchers components) {
        return entity != null && components.test(entity.collectComponents());
    }

    public boolean requiresNbt() {
        return this.nbt.isPresent();
    }

    public static class Builder {
        private Optional<HolderSet<Block>> blocks = Optional.empty();
        private Optional<StatePropertiesPredicate> properties = Optional.empty();
        private Optional<NbtPredicate> nbt = Optional.empty();
        private DataComponentMatchers components = DataComponentMatchers.ANY;

        private Builder() {
        }

        public static Builder block() {
            return new Builder();
        }

        public Builder of(HolderGetter<Block> lookup, Block ... blocks) {
            return this.of(lookup, Arrays.asList(blocks));
        }

        public Builder of(HolderGetter<Block> lookup, Collection<Block> blocks) {
            this.blocks = Optional.of(HolderSet.direct(Block::builtInRegistryHolder, blocks));
            return this;
        }

        public Builder of(HolderGetter<Block> lookup, TagKey<Block> tag) {
            this.blocks = Optional.of(lookup.getOrThrow(tag));
            return this;
        }

        public Builder hasNbt(CompoundTag nbt) {
            this.nbt = Optional.of(new NbtPredicate(nbt));
            return this;
        }

        public Builder setProperties(StatePropertiesPredicate.Builder properties) {
            this.properties = properties.build();
            return this;
        }

        public Builder components(DataComponentMatchers components) {
            this.components = components;
            return this;
        }

        public BlockPredicate build() {
            return new BlockPredicate(this.blocks, this.properties, this.nbt, this.components);
        }
    }
}


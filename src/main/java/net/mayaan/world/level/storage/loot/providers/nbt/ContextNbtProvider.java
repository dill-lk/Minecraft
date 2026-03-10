/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.storage.loot.providers.nbt;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.mayaan.advancements.criterion.NbtPredicate;
import net.mayaan.nbt.Tag;
import net.mayaan.util.context.ContextKey;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootContextArg;
import net.mayaan.world.level.storage.loot.providers.nbt.NbtProvider;
import org.jspecify.annotations.Nullable;

public class ContextNbtProvider
implements NbtProvider {
    private static final Codec<LootContextArg<Tag>> GETTER_CODEC = LootContextArg.createArgCodec(builder -> builder.anyBlockEntity(BlockEntitySource::new).anyEntity(EntitySource::new));
    public static final MapCodec<ContextNbtProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GETTER_CODEC.fieldOf("target").forGetter(p -> p.source)).apply((Applicative)i, ContextNbtProvider::new));
    public static final Codec<ContextNbtProvider> INLINE_CODEC = GETTER_CODEC.xmap(ContextNbtProvider::new, p -> p.source);
    private final LootContextArg<Tag> source;

    private ContextNbtProvider(LootContextArg<Tag> source) {
        this.source = source;
    }

    public MapCodec<ContextNbtProvider> codec() {
        return MAP_CODEC;
    }

    @Override
    public @Nullable Tag get(LootContext context) {
        return this.source.get(context);
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(this.source.contextParam());
    }

    public static NbtProvider forContextEntity(LootContext.EntityTarget source) {
        return new ContextNbtProvider(new EntitySource(source.contextParam()));
    }

    private record EntitySource(ContextKey<? extends Entity> contextParam) implements LootContextArg.Getter<Entity, Tag>
    {
        @Override
        public Tag get(Entity entity) {
            return NbtPredicate.getEntityTagToCompare(entity);
        }
    }

    private record BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) implements LootContextArg.Getter<BlockEntity, Tag>
    {
        @Override
        public Tag get(BlockEntity blockEntity) {
            return blockEntity.saveWithFullMetadata(blockEntity.getLevel().registryAccess());
        }
    }
}


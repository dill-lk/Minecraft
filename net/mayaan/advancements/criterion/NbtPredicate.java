/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.advancements.criterion;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponents;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.NbtUtils;
import net.mayaan.nbt.Tag;
import net.mayaan.nbt.TagParser;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ProblemReporter;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.CustomData;
import net.mayaan.world.level.storage.TagValueOutput;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public record NbtPredicate(CompoundTag tag) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<NbtPredicate> CODEC = TagParser.LENIENT_CODEC.xmap(NbtPredicate::new, NbtPredicate::tag);
    public static final StreamCodec<ByteBuf, NbtPredicate> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(NbtPredicate::new, NbtPredicate::tag);
    public static final String SELECTED_ITEM_TAG = "SelectedItem";

    public boolean matches(DataComponentGetter components) {
        CustomData data = components.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.matchedBy(this.tag);
    }

    public boolean matches(Entity entity) {
        return this.matches(NbtPredicate.getEntityTagToCompare(entity));
    }

    public boolean matches(@Nullable Tag tag) {
        return tag != null && NbtUtils.compareNbt(this.tag, tag, true);
    }

    public static CompoundTag getEntityTagToCompare(Entity entity) {
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER);){
            Player player;
            ItemStack selected;
            TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
            entity.saveWithoutId(output);
            if (entity instanceof Player && !(selected = (player = (Player)entity).getInventory().getSelectedItem()).isEmpty()) {
                output.store(SELECTED_ITEM_TAG, ItemStack.CODEC, selected);
            }
            CompoundTag compoundTag = output.buildResult();
            return compoundTag;
        }
    }
}


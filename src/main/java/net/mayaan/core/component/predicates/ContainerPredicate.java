/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.core.component.predicates;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.criterion.CollectionPredicate;
import net.mayaan.advancements.criterion.ItemPredicate;
import net.mayaan.advancements.criterion.SingleComponentItemPredicate;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.world.item.ItemInstance;
import net.mayaan.world.item.component.ItemContainerContents;

public record ContainerPredicate(Optional<CollectionPredicate<ItemInstance, ItemPredicate>> items) implements SingleComponentItemPredicate<ItemContainerContents>
{
    public static final Codec<ContainerPredicate> CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(ContainerPredicate::items)).apply((Applicative)i, ContainerPredicate::new));

    @Override
    public DataComponentType<ItemContainerContents> componentType() {
        return DataComponents.CONTAINER;
    }

    @Override
    public boolean matches(ItemContainerContents value) {
        return !this.items.isPresent() || this.items.get().test(value.nonEmptyItems());
    }
}


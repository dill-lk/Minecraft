/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.trading;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponentExactPredicate;
import net.mayaan.util.Mth;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.trading.ItemCost;
import net.mayaan.world.level.ItemLike;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContext;
import net.mayaan.world.level.storage.loot.providers.number.ConstantValue;
import net.mayaan.world.level.storage.loot.providers.number.NumberProvider;
import net.mayaan.world.level.storage.loot.providers.number.NumberProviders;

public record TradeCost(Holder<Item> item, NumberProvider count, DataComponentExactPredicate components) implements Validatable
{
    public static final Codec<TradeCost> CODEC = RecordCodecBuilder.create(i -> i.group((App)Item.CODEC.fieldOf("id").forGetter(TradeCost::item), (App)NumberProviders.CODEC.optionalFieldOf("count", (Object)ConstantValue.exactly(1.0f)).forGetter(TradeCost::count), (App)DataComponentExactPredicate.CODEC.optionalFieldOf("components", (Object)DataComponentExactPredicate.EMPTY).forGetter(TradeCost::components)).apply((Applicative)i, TradeCost::new));

    public TradeCost(ItemLike item, int count) {
        this(item.asItem().builtInRegistryHolder(), ConstantValue.exactly(count), DataComponentExactPredicate.EMPTY);
    }

    public TradeCost(ItemLike item, NumberProvider count) {
        this(item.asItem().builtInRegistryHolder(), count, DataComponentExactPredicate.EMPTY);
    }

    public ItemCost toItemCost(LootContext lootContext, int additionalCost) {
        int count = Mth.clamp(this.count().getInt(lootContext) + additionalCost, 0, this.item().value().getDefaultMaxStackSize());
        return new ItemCost(this.item(), count, this.components());
    }

    @Override
    public void validate(ValidationContext context) {
        Validatable.validate(context, "count", this.count);
    }
}


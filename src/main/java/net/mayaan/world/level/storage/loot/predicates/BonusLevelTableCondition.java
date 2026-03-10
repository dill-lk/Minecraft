/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.storage.loot.predicates;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.mayaan.core.Holder;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.context.ContextKey;
import net.mayaan.world.item.ItemInstance;
import net.mayaan.world.item.enchantment.Enchantment;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.parameters.LootContextParams;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;

public record BonusLevelTableCondition(Holder<Enchantment> enchantment, List<Float> values) implements LootItemCondition
{
    public static final MapCodec<BonusLevelTableCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(BonusLevelTableCondition::enchantment), (App)ExtraCodecs.nonEmptyList(Codec.FLOAT.listOf()).fieldOf("chances").forGetter(BonusLevelTableCondition::values)).apply((Applicative)i, BonusLevelTableCondition::new));

    public MapCodec<BonusLevelTableCondition> codec() {
        return MAP_CODEC;
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.TOOL);
    }

    @Override
    public boolean test(LootContext context) {
        ItemInstance tool = context.getOptionalParameter(LootContextParams.TOOL);
        int level = tool != null ? EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool) : 0;
        float chance = this.values.get(Math.min(level, this.values.size() - 1)).floatValue();
        return context.getRandom().nextFloat() < chance;
    }

    public static LootItemCondition.Builder bonusLevelFlatChance(Holder<Enchantment> enchantment, float ... chances) {
        ArrayList<Float> chancesList = new ArrayList<Float>(chances.length);
        for (float chance : chances) {
            chancesList.add(Float.valueOf(chance));
        }
        return () -> new BonusLevelTableCondition(enchantment, chancesList);
    }
}


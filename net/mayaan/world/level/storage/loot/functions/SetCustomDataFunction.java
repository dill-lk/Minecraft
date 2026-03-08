/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.storage.loot.functions;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.core.component.DataComponents;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.TagParser;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.CustomData;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;

public class SetCustomDataFunction
extends LootItemConditionalFunction {
    public static final MapCodec<SetCustomDataFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> SetCustomDataFunction.commonFields(i).and((App)TagParser.LENIENT_CODEC.fieldOf("tag").forGetter(f -> f.tag)).apply((Applicative)i, SetCustomDataFunction::new));
    private final CompoundTag tag;

    private SetCustomDataFunction(List<LootItemCondition> predicates, CompoundTag tag) {
        super(predicates);
        this.tag = tag;
    }

    public MapCodec<SetCustomDataFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext context) {
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.merge(this.tag));
        return itemStack;
    }

    @Deprecated
    public static LootItemConditionalFunction.Builder<?> setCustomData(CompoundTag value) {
        return SetCustomDataFunction.simpleBuilder(conditions -> new SetCustomDataFunction((List<LootItemCondition>)conditions, value));
    }
}


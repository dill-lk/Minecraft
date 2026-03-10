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
import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.tags.TagKey;
import net.mayaan.world.item.Instrument;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.InstrumentComponent;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;

public class SetInstrumentFunction
extends LootItemConditionalFunction {
    public static final MapCodec<SetInstrumentFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> SetInstrumentFunction.commonFields(i).and((App)TagKey.hashedCodec(Registries.INSTRUMENT).fieldOf("options").forGetter(f -> f.options)).apply((Applicative)i, SetInstrumentFunction::new));
    private final TagKey<Instrument> options;

    private SetInstrumentFunction(List<LootItemCondition> predicates, TagKey<Instrument> options) {
        super(predicates);
        this.options = options;
    }

    public MapCodec<SetInstrumentFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext context) {
        HolderLookup.RegistryLookup instruments = context.getLevel().registryAccess().lookupOrThrow(Registries.INSTRUMENT);
        Optional instrument = instruments.getRandomElementOf(this.options, context.getRandom());
        if (instrument.isPresent()) {
            itemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent((Holder)instrument.get()));
        }
        return itemStack;
    }

    public static LootItemConditionalFunction.Builder<?> setInstrumentOptions(TagKey<Instrument> options) {
        return SetInstrumentFunction.simpleBuilder(conditions -> new SetInstrumentFunction((List<LootItemCondition>)conditions, options));
    }
}


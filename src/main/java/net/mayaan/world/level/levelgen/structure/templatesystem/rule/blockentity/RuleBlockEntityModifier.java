/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.levelgen.structure.templatesystem.rule.blockentity;

import com.mojang.serialization.Codec;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import org.jspecify.annotations.Nullable;

public interface RuleBlockEntityModifier {
    public static final Codec<RuleBlockEntityModifier> CODEC = BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER.byNameCodec().dispatch(RuleBlockEntityModifier::getType, RuleBlockEntityModifierType::codec);

    public @Nullable CompoundTag apply(RandomSource var1, @Nullable CompoundTag var2);

    public RuleBlockEntityModifierType<?> getType();
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.structure.templatesystem.RuleTestType;

public abstract class RuleTest {
    public static final Codec<RuleTest> CODEC = BuiltInRegistries.RULE_TEST.byNameCodec().dispatch("predicate_type", RuleTest::getType, RuleTestType::codec);

    public abstract boolean test(BlockState var1, RandomSource var2);

    protected abstract RuleTestType<?> getType();
}


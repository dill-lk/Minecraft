/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.structure.templatesystem.PosRuleTestType;

public abstract class PosRuleTest {
    public static final Codec<PosRuleTest> CODEC = BuiltInRegistries.POS_RULE_TEST.byNameCodec().dispatch("predicate_type", PosRuleTest::getType, PosRuleTestType::codec);

    public abstract boolean test(BlockPos var1, BlockPos var2, BlockPos var3, RandomSource var4);

    protected abstract PosRuleTestType<?> getType();
}


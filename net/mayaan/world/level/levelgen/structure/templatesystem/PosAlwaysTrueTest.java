/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.mayaan.world.level.levelgen.structure.templatesystem.PosRuleTestType;

public class PosAlwaysTrueTest
extends PosRuleTest {
    public static final MapCodec<PosAlwaysTrueTest> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final PosAlwaysTrueTest INSTANCE = new PosAlwaysTrueTest();

    private PosAlwaysTrueTest() {
    }

    @Override
    public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, RandomSource random) {
        return true;
    }

    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.ALWAYS_TRUE_TEST;
    }
}


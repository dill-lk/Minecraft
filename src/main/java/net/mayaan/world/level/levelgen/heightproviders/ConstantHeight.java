/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.heightproviders;

import com.mojang.serialization.MapCodec;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.VerticalAnchor;
import net.mayaan.world.level.levelgen.WorldGenerationContext;
import net.mayaan.world.level.levelgen.heightproviders.HeightProvider;
import net.mayaan.world.level.levelgen.heightproviders.HeightProviderType;

public class ConstantHeight
extends HeightProvider {
    public static final ConstantHeight ZERO = new ConstantHeight(VerticalAnchor.absolute(0));
    public static final MapCodec<ConstantHeight> CODEC = VerticalAnchor.CODEC.fieldOf("value").xmap(ConstantHeight::new, ConstantHeight::getValue);
    private final VerticalAnchor value;

    public static ConstantHeight of(VerticalAnchor value) {
        return new ConstantHeight(value);
    }

    private ConstantHeight(VerticalAnchor value) {
        this.value = value;
    }

    public VerticalAnchor getValue() {
        return this.value;
    }

    @Override
    public int sample(RandomSource random, WorldGenerationContext context) {
        return this.value.resolveY(context);
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.CONSTANT;
    }

    public String toString() {
        return this.value.toString();
    }
}


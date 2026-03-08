/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.util.valueproviders;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.valueproviders.ClampedNormalFloat;
import net.mayaan.util.valueproviders.ConstantFloat;
import net.mayaan.util.valueproviders.FloatProvider;
import net.mayaan.util.valueproviders.TrapezoidFloat;
import net.mayaan.util.valueproviders.UniformFloat;

public interface FloatProviderType<P extends FloatProvider> {
    public static final FloatProviderType<ConstantFloat> CONSTANT = FloatProviderType.register("constant", ConstantFloat.CODEC);
    public static final FloatProviderType<UniformFloat> UNIFORM = FloatProviderType.register("uniform", UniformFloat.CODEC);
    public static final FloatProviderType<ClampedNormalFloat> CLAMPED_NORMAL = FloatProviderType.register("clamped_normal", ClampedNormalFloat.CODEC);
    public static final FloatProviderType<TrapezoidFloat> TRAPEZOID = FloatProviderType.register("trapezoid", TrapezoidFloat.CODEC);

    public MapCodec<P> codec();

    public static <P extends FloatProvider> FloatProviderType<P> register(String id, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.FLOAT_PROVIDER_TYPE, id, () -> codec);
    }
}


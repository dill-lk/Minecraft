/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.mayaan.core.BlockPos;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.placement.PlacementContext;
import net.mayaan.world.level.levelgen.placement.PlacementModifierType;

public abstract class PlacementModifier {
    public static final Codec<PlacementModifier> CODEC = BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.byNameCodec().dispatch(PlacementModifier::type, PlacementModifierType::codec);

    public abstract Stream<BlockPos> getPositions(PlacementContext var1, RandomSource var2, BlockPos var3);

    public abstract PlacementModifierType<?> type();
}


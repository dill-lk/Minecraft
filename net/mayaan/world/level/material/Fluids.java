/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.material;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.level.material.EmptyFluid;
import net.mayaan.world.level.material.FlowingFluid;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.LavaFluid;
import net.mayaan.world.level.material.WaterFluid;

public class Fluids {
    public static final Fluid EMPTY = Fluids.register("empty", new EmptyFluid());
    public static final FlowingFluid FLOWING_WATER = Fluids.register("flowing_water", new WaterFluid.Flowing());
    public static final FlowingFluid WATER = Fluids.register("water", new WaterFluid.Source());
    public static final FlowingFluid FLOWING_LAVA = Fluids.register("flowing_lava", new LavaFluid.Flowing());
    public static final FlowingFluid LAVA = Fluids.register("lava", new LavaFluid.Source());

    private static <T extends Fluid> T register(String name, T fluid) {
        return (T)Registry.register(BuiltInRegistries.FLUID, name, fluid);
    }

    static {
        for (Fluid fluid : BuiltInRegistries.FLUID) {
            for (FluidState state : fluid.getStateDefinition().getPossibleStates()) {
                Fluid.FLUID_STATE_REGISTRY.add(state);
            }
        }
    }
}


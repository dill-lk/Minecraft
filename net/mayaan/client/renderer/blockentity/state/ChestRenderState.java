/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.level.block.state.properties.ChestType;

public class ChestRenderState
extends BlockEntityRenderState {
    public ChestType type = ChestType.SINGLE;
    public float open;
    public Direction facing = Direction.SOUTH;
    public ChestMaterialType material = ChestMaterialType.REGULAR;

    public static enum ChestMaterialType {
        ENDER_CHEST,
        CHRISTMAS,
        TRAPPED,
        COPPER_UNAFFECTED,
        COPPER_EXPOSED,
        COPPER_WEATHERED,
        COPPER_OXIDIZED,
        REGULAR;

    }
}


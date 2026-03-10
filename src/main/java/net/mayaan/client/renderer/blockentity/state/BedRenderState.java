/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.block.state.properties.BedPart;

public class BedRenderState
extends BlockEntityRenderState {
    public DyeColor color = DyeColor.WHITE;
    public Direction facing = Direction.NORTH;
    public BedPart part;
}


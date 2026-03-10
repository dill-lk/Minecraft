/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.item.DyeColor;
import org.jspecify.annotations.Nullable;

public class ShulkerBoxRenderState
extends BlockEntityRenderState {
    public Direction direction = Direction.NORTH;
    public @Nullable DyeColor color;
    public float progress;
}


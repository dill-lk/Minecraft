/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.core.Direction;
import org.jspecify.annotations.Nullable;

public class ShelfRenderState
extends BlockEntityRenderState {
    public final @Nullable ItemStackRenderState[] items = new ItemStackRenderState[3];
    public boolean alignToBottom;
    public Direction facing = Direction.NORTH;
}


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

public class BrushableBlockRenderState
extends BlockEntityRenderState {
    public final ItemStackRenderState itemState = new ItemStackRenderState();
    public int dustProgress;
    public @Nullable Direction hitDirection;
}


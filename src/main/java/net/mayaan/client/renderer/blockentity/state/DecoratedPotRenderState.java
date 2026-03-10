/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.level.block.entity.PotDecorations;
import org.jspecify.annotations.Nullable;

public class DecoratedPotRenderState
extends BlockEntityRenderState {
    public float yRot;
    public  @Nullable DecoratedPotBlockEntity.WobbleStyle wobbleStyle;
    public float wobbleProgress;
    public PotDecorations decorations = PotDecorations.EMPTY;
    public Direction direction = Direction.NORTH;
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.entity.decoration.painting.PaintingVariant;
import org.jspecify.annotations.Nullable;

public class PaintingRenderState
extends EntityRenderState {
    public Direction direction = Direction.NORTH;
    public @Nullable PaintingVariant variant;
    public int[] lightCoordsPerBlock = new int[0];
}


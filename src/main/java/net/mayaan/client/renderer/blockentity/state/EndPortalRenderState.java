/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import java.util.EnumSet;
import java.util.Set;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.Direction;

public class EndPortalRenderState
extends BlockEntityRenderState {
    public final Set<Direction> facesToShow = EnumSet.noneOf(Direction.class);
}

